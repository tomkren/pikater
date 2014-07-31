package org.pikater.web.vaadin.gui.server.components.console;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.IllegalSelectorException;

import org.fusesource.jansi.HtmlAnsiOutputStream;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.ssh.SSHRemoteExec;
import org.pikater.web.ssh.SSHSession;
import org.pikater.web.ssh.SSHBatchExecChannel.ISSHBatchChannelNotificationHandler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.jcraft.jsch.JSchException;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("simpleConsoleComponent.css")
public class SimpleConsoleComponent extends VerticalLayout
{
	private static final long serialVersionUID = 9141220076403095003L;

	public enum MessageStyle
	{
		CASUAL,
		ERROR
	};
	
	// GUI
	private final Panel outputFieldScrollPanel;
	private final Label outputField;
	private final TextField inputField;

	// other variables
	private final StringBuilder consoleOutputLog;
	private final SSHRemoteExec remoteExec;

	public SimpleConsoleComponent(final SSHSession session)
	{
		super();
		setSizeFull();

		// first setup GUI
		this.inputField = new TextField();
		this.inputField.setWidth("100%");
		this.inputField.addStyleName("consoleComponentInputField"); // adds a min and max height
		this.inputField.addShortcutListener(new ShortcutListener("", KeyCodes.KEY_ENTER, null)
		{
			private static final long serialVersionUID = 3588193311013333196L;

			@Override
			public void handleAction(Object sender, Object target)
			{
				if(execAsync(inputField.getValue())) // no errors sending the command
				{
					inputField.setValue(""); // clear in the input field in the meantime
				}
			}
		});
		this.outputField = new Label("", ContentMode.HTML);
		this.outputField.setWidth("100%");
		this.outputField.setStyleName("consoleComponentOutput");
		// this.outputField.setSizeUndefined();
		
		this.outputFieldScrollPanel = new Panel();
		this.outputFieldScrollPanel.setContent(this.outputField);
		this.outputFieldScrollPanel.setSizeFull();
		
		addComponent(outputFieldScrollPanel);
		addComponent(inputField);
		setComponentAlignment(inputField, Alignment.BOTTOM_CENTER);
		setExpandRatio(outputFieldScrollPanel, 1);

		// and finally, create the console, event handlers and other variables
		ISSHBatchChannelNotificationHandler notificationHandler = new ISSHBatchChannelNotificationHandler()
		{
			@Override
			public void handle(byte[] bytes, int offset, int count)
			{
				appendMessage(MessageStyle.CASUAL, new String(bytes, offset, count));

				if(SimpleConsoleComponent.this.isAttached())
				{
					// don't forget that this thread has not been initiated by a user request so we have to push the changes to the client - but only if attached
					getUI().access(new Runnable()
					{
						@Override
						public void run()
						{
							getUI().push(); // sort of like commit - only works this way if PushMode is set to MANUAL
						}
					});
				}
			}
		};
		this.remoteExec = new SSHRemoteExec(session, notificationHandler);
		this.consoleOutputLog = new StringBuilder();
	}
	
	// --------------------------------------------------------------------------------------------
	// PUBLIC INTERFACE
	
	/*
	 * Use this method, when you ARE interested in the response. If you're NOT, use {@link #execAsync} instead.
	 * @param commands Navigate to the referenced execSync method to display more information.
	 * @return The response to the commands wrapped in a special class. If the commands are not executed, the errors are logged.
	 * @see org.pikater.shared.ssh.SSHRemoteExec#execSync(String command)
	public ExecSyncResult execSync(String commands)
	{
		try
		{
			return new ExecSyncResult(remoteExec.execSync(commands), true);
		}
		catch (JSchException e)
		{
			// don't log this, it is most likely a user related error
			remoteExec.getSession().getNotificationHandler().handleError(
					String.format("Commands could not be executed and probably are invalid: '%s'", commands),
					e); // forward to session handler
			return new ExecSyncResult(null, false);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ExecSyncResult(null, false);
		}
	}
	*/
	
	/**
	 * Use this method, when you're NOT interested in the response. If you ARE, use {@link #execSync} instead.
	 * @param command Navigate to the referenced execSync method to display more information about this parameter.
	 * @return True, if the commands were sent to the command output stream successfully, otherwise false and corresponding errors are logged.
	 * @see org.pikater.web.ssh.SSHRemoteExec#execSync(String command)
	 */
	public boolean execAsync(String commands)
	{
		try
		{
			remoteExec.execAsync(commands); // it will be sent and executed remotely sometime later
			return true;
		}
		catch (JSchException e)
		{
			// don't log this, it is most likely a user related error
			remoteExec.getSession().getNotificationHandler().handleError(
					String.format("Commands could not be executed and are probably invalid: '%s'", commands),
					e); // forward to session handler
			return false;
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable("Could not write command to the console input stream. Weird...", e);
			remoteExec.getSession().getNotificationHandler().handleError("could not execute the last command", e); // forward to session handler
			return false;
		}
	}
	
	public void appendMessage(MessageStyle style, String text)
	{
		appendMessageInternal(style, text);

		// update the console output log
		outputField.setValue(consoleOutputLog.toString());
		 
		if(this.isAttached())
		{
			// scroll to the bottom of the view
			outputFieldScrollPanel.setScrollTop(Short.MAX_VALUE); // Integer.MAX_VALUE seems to be too high and it doesn't work anymore 
		}
	}
	
	public void disableInputField()
	{
		inputField.setEnabled(false);
	}
	
	// --------------------------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void appendMessageInternal(MessageStyle style, String text)
	{
		switch (style)
		{
			case CASUAL:
				appendText(text);
				break;
			case ERROR:
				appendText("<br><br><span style=\"color: red;\">ERROR: " + text + "</span><br><br>");
				break;
			default:
				throw new IllegalSelectorException();
		}
	}
	
	private synchronized void appendText(String text)
	{
		consoleOutputLog.append(ansiToHTML(text.getBytes(), 0, text.length()));
	}
	
	private String ansiToHTML(byte[] bytes, int offset, int count)
	{
		MyOutputStream result = new MyOutputStream();
		try
		{
			new HtmlAnsiOutputStream(result).write(bytes, offset, count);
			// System.out.println(result.toString());
			return result.toString();
		}
		catch (IOException e)
		{
			// no way this can occur
			return null;
		}
		// no need to close the stream if it's not connected to a resource 
	}
	
	// --------------------------------------------------------------------------------------------
	// PRIVATE TYPES

	private class MyOutputStream extends OutputStream
	{
		private final StringBuilder buff;

		public MyOutputStream()
		{
			this.buff = new StringBuilder();
		}

		@Override
		public void write(int b) throws IOException
		{
			switch ((char)b)
			{
				case '\n': // omit
					break;
				case '\r': // replace for <br> 
					buff.append("<br>");
					break;
				default:
					buff.append((char)b);
					break;
			}
		}

		@Override
		public String toString()
		{
			return buff.toString();
		}
	}
}	
