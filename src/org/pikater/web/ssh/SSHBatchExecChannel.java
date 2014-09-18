package org.pikater.web.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.pikater.shared.logging.web.PikaterWebLogger;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;

public final class SSHBatchExecChannel implements ISSHChannel, ISSHAsyncCommandExec
{
	private static final int consoleOutputRefreshRate = 100; // in milliseconds
	
	private final SSHSession session;
	private final ISSHBatchChannelNotificationHandler outputHandler;
	private ChannelShell shellChannel;
	
	private ProgramOutputToConsoleInputStream commandStream;
	private ConsoleOutputToProgramInputStream responseStream;
	
	public SSHBatchExecChannel(SSHSession session, ISSHBatchChannelNotificationHandler outputHandler, boolean processOutput)
	{
		this.session = session;
		this.outputHandler = outputHandler;
		
		try
		{
			this.shellChannel = (ChannelShell) session.openChannel("shell"); // shell channel capable of executing arbitrary amount of commands one command after another
			this.commandStream = new ProgramOutputToConsoleInputStream();
			this.responseStream = new ConsoleOutputToProgramInputStream();
			
			this.shellChannel.setInputStream(commandStream.getPipedInputForNextProgram());
			if(processOutput)
			{
				this.shellChannel.setOutputStream(responseStream);
			}
		}
		catch (JSchException e)
		{
			session.getNotificationHandler().handleError("Could not open a shell channel from the created session.", e);
			this.shellChannel = null;
			return;
		}
		catch (IOException e)
		{
			session.getNotificationHandler().handleError("Could not connect the piped output and input streams. Have you changed "
					+ "the implementation recently? Otherwise this error should not be happening.", e);
			this.shellChannel = null;
			return;
		}
		
		try
		{
			this.shellChannel.connect();
			responseStream.start();
		}
		catch (JSchException e)
		{
			session.getNotificationHandler().handleError("Could not establish the channel connection.", e);
			this.shellChannel = null;
			return;
		}
	}
	
	//------------------------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public void execAsync(String command) throws JSchException, IOException
	{
		if(shellChannel != null)
		{
			// command will not execute on the remote machine if it doesn't end with newline
			commandStream.write((command.trim() + "\n").getBytes());
		}
		else
		{
			throw new JSchException("Channel has not been initialized properly due to an error. Resolve it and try again.");
		}
	}
	
	@Override
	public SSHSession getSession()
	{
		return session;
	}
	
	@Override
	public synchronized void close() throws JSchException
	{
		if(shellChannel != null)
		{
			if(!shellChannel.isClosed())
			{
				shellChannel.disconnect();
				try
				{
					commandStream.close();
					responseStream.close();
				}
				catch (IOException e)
				{
					PikaterWebLogger.logThrowable("An error occured while closing the channel's related streams.", e);
				}
			}
		}
		else
		{
			throw new JSchException("Channel has not been initialized properly due to an error. Resolve it and try again.");
		}
	}
	
	//------------------------------------------------------------------------------
	// PUBLIC TYPES:
	
	public interface ISSHBatchChannelNotificationHandler
	{
		void handle(byte[] bytes, int offset, int count);
	}
	
	//------------------------------------------------------------------------------
	// PRIVATE TYPES:
	
	private class ProgramOutputToConsoleInputStream extends PipedOutputStream
	{
		private final PipedInputStream inputForNextProgram;

		public ProgramOutputToConsoleInputStream() throws IOException
		{
			super();
			this.inputForNextProgram = new PipedInputStream();
			this.connect(this.inputForNextProgram);
		}
		
		public InputStream getPipedInputForNextProgram()
		{
			return inputForNextProgram;
		}
		
		@Override
		public void close() throws IOException
		{
			inputForNextProgram.close();
			super.close();
		}
	}
	
	/**
	 *  The "PipedOutputStream" this class extends will delegate the console output to the underlying "PipedInputStream" from which the responses will be read.
	 */
	private class ConsoleOutputToProgramInputStream extends PipedOutputStream
	{
		private final PipedInputStream consoleOutput;
		private final Thread localResponseThread;
		
		private boolean started;
		
		public ConsoleOutputToProgramInputStream() throws IOException
		{
			super();
			this.consoleOutput = new PipedInputStream();
			this.connect(this.consoleOutput);
			this.localResponseThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					runLocalResponseThread();
				}
			});
			this.started = false;
		}
		
		/**
		 * Starts the local reading thread. Only call this when the channel is initialized and connected.
		 */
		public void start()
		{
			if(!started)
			{
				localResponseThread.start();
				started = true;
			}
		}
		
		// ------------------------------------------------------------------------
		// PRIVATE INTERFACE
		
		private void runLocalResponseThread()
		{
			byte[] tmp = new byte[1024];
			try
			{
				while(true)
				{
					while (consoleOutput.available() > 0)
					{
						int bytesRead = consoleOutput.read(tmp, 0, 1024);
						if (bytesRead > 0)
						{
							outputHandler.handle(tmp, 0, bytesRead);
						}
						else if (bytesRead < 0)
						{
							break;
						}
					}
					if(shellChannel.isClosed())
					{
						session.getNotificationHandler().notifyChannelClosed(shellChannel.getExitStatus());
						break;
					}
					else
					{
						Thread.sleep(consoleOutputRefreshRate);
					}
				}
			}
			catch (IOException e)
			{
				session.getNotificationHandler().handleError("Either an internal InputStream error occured, or the remote "
						+ "machine closed connection or it got interrupted in some other way.", e);
			}
			catch (InterruptedException e)
			{
				session.getNotificationHandler().handleError("The thread reading console output has been interrupted (likely by the system).", e);
			}
			finally
			{
				try
				{
					SSHBatchExecChannel.this.close(); // may have no effect
					this.consoleOutput.close();
					this.close();
				}
				catch (IOException e)
				{
				}
				catch (JSchException e)
				{
				}
			}
		}
	}
}