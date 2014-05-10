package org.pikater.web.pikater;

import org.pikater.web.ExecSequence;
import org.pikater.web.vaadin.gui.server.components.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.SimpleConsoleComponent.MessageStyle;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem;
import org.pikater.web.vaadin.gui.server.welcometour.RemoteServerInfoItem.Header;
import org.pikater.shared.FieldVerifier;
import org.pikater.shared.RemoteServerInfo.FieldType;
import org.pikater.shared.ssh.SSHSession;

public class PikaterSSHLauncher
{
	private static int slaveID = 1;
	
	/**
	 * Class providing all the information for making the connection and launching.
	 */
	public final RemoteServerInfoItem serverInfo;
	
	/**
	 * Tools for connection making and pikater launching.
	 */
	private SSHSession session;
	
	/**
	 * Component to write output to.
	 */
	private SimpleConsoleComponent outputConsoleComponent;
	
	public PikaterSSHLauncher(RemoteServerInfoItem serverInfo)
	{
		super();
		this.session = null;
		this.serverInfo = serverInfo;
	}
	
	public SimpleConsoleComponent getOutputConsoleComponent()
	{
		return outputConsoleComponent;
	}
	
	public boolean tryConnect(boolean test_forceReturnTrue)
	{
		if(isConnected())
		{
			return true;
		}
		else
		{
			// create session and connect to the remote machine
			SSHSession newSession = new SSHSession(
					serverInfo.underlyingInfoInstance.getField(FieldType.HOSTNAME),
					serverInfo.underlyingInfoInstance.getField(FieldType.FINGERPRINT),
					serverInfo.underlyingInfoInstance.getField(FieldType.USERNAME),
					serverInfo.underlyingInfoInstance.getField(FieldType.PASSWORD),
					new SSHSession.ISSHSessionNotificationHandler()
					{
						@Override
						public void notifySessionClosed()
						{
							PikaterSSHLauncher.this.handleError("session closed from remote", true);
						}
						
						@Override
						public void notifyChannelClosed(int exitStatus)
						{
							PikaterSSHLauncher.this.handleError(String.format("channel closed from remote with exit status '%d'", exitStatus), true);
						}
						
						@Override
						public void handleError(String description, Throwable t)
						{
							PikaterSSHLauncher.this.handleError(description + "\n" + t.getMessage(), false);
						}
					}
			);
			
			if(test_forceReturnTrue)
			{
				updateConnectionStatus(RemoteServerInfoItem.connectionStatus_canConnect);
				return true;
			}
			else if(newSession.isAliveAndWell())
			{
				this.session = newSession;
				updateConnectionStatus(RemoteServerInfoItem.connectionStatus_canConnect);
				return true;
			}
			else
			{
				updateConnectionStatus(RemoteServerInfoItem.connectionStatus_canNotConnect);
				newSession.closeSession();
				return false;
			}
		}
	}
	
	public boolean isConnected()
	{
		return (session != null) && session.isAliveAndWell();
	}
	
	public void close()
	{
		if(isConnected())
		{
			session.closeSession();
		}
	}
	
	public boolean launchPikater()
	{
		/*
		 * If connection doesn't exist yet, try to create it. If pikater has already been launched, don't try again.
		 */
		if((!isConnected() && tryConnect(false)) || isConnected())
		{
			// all errors are forwarded to the @handleError method of @SSHSession.ISSHSessionNotificationHandler - see above
			this.outputConsoleComponent = new SimpleConsoleComponent(session);
			
			// now launch pikater
			switch (serverInfo.getServerType())
			{
				case MASTER:
					return launchPikaterInMasterMode();
					// return false;
				case SLAVE:
					return launchPikaterInSlaveMode();
					// return false;
				default:
					throw new IllegalStateException();
			}
		}
		else
		{
			return false;
		}
	}
	
	// -----------------------------------------------------------
	// PRIVATE INTERFACE
	
	private int getNextSlaveID()
	{
		return slaveID++;
	}
	
	private boolean launchPikaterInMasterMode()
	{
		return doLaunchPikater("./runPikaterCoreMaster.sh");
	}
	
	private boolean launchPikaterInSlaveMode()
	{
		return doLaunchPikater("./runPikaterCoreSlave.sh " + String.valueOf(getNextSlaveID()));
	}
	
	private boolean doLaunchPikater(String finalLaunchingCommand)
	{
		ExecSequence commands = new ExecSequence(true);
		
		/*
		 * Gradually define the command sequence.
		 */
		
		// first change the working directory if need be
		String directory = serverInfo.underlyingInfoInstance.getField(FieldType.DIRECTORY);
		if(!FieldVerifier.isStringNullOrEmpty(directory))
		{
			commands.enqueueCommand(String.format("cd \"%s\"", directory));
		}
		
		// the rest of the commands
		commands.enqueueCommand("git clone https://github.com/krajj7/pikater");
		commands.enqueueCommand("cd pikater/core");
		commands.enqueueCommand("./buildPikaterCore.sh");
		commands.enqueueCommand(finalLaunchingCommand);
		
		/*
		 * And now try to execute them in the defined order.
		 */
		
		if(commands.exec(outputConsoleComponent)) // commands successfully sent for execution - execution errors are not handled here
		{
			updateConnectionStatus(RemoteServerInfoItem.connectionStatus_launched);
			return true;
		}
		else // an error occured while trying to send a command for execution
		{
			handleError(String.format("Pikater launching failed at command: '%s'", commands.getFailedCommand()), false);
			updateConnectionStatus(RemoteServerInfoItem.connectionStatus_error);
			return false;
		}
	}
	
	private void updateConnectionStatus(String newValue)
	{
		serverInfo.setValueForProperty(Header.STATUS, newValue);
	}
	
	private void handleError(String errorMessage, boolean blockFurtherCommands)
	{
		if(outputConsoleComponent != null)
		{
			outputConsoleComponent.appendMessage(MessageStyle.ERROR, errorMessage);
			updateConnectionStatus(RemoteServerInfoItem.connectionStatus_error);
			if(blockFurtherCommands)
			{
				outputConsoleComponent.disableInputField();
			}
		}
		else
		{
			// TODO:
		}
	}
}
