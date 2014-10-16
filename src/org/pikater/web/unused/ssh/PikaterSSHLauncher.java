package org.pikater.web.unused.ssh;

import org.pikater.web.unused.components.console.SimpleConsoleComponent;
import org.pikater.web.unused.components.console.SimpleConsoleComponent.MessageStyle;
import org.pikater.web.unused.welcometour.RemoteServerInfoItem;
import org.pikater.web.unused.welcometour.RemoteServerInfoItem.Header;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.shared.FieldVerifier;
import org.pikater.shared.RemoteServerInfo.FieldType;

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
		this.serverInfo = serverInfo;
		this.session = null;
		this.outputConsoleComponent = null;
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
				updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_CAN_CONNECT);
				return true;
			}
			else if(newSession.isAliveAndWell())
			{
				this.session = newSession;
				updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_CAN_CONNECT);
				return true;
			}
			else
			{
				updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_CAN_NOT_CONNECT);
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
		return doLaunchPikater(String.format("./runPikaterCoreSlave.sh %d", getNextSlaveID()));
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
			updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_LAUNCHED);
			return true;
		}
		else // an error occured while trying to send a command for execution
		{
			handleError(String.format("Pikater launching failed at command: '%s'", commands.getFailedCommand()), false);
			updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_ERROR);
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
			updateConnectionStatus(RemoteServerInfoItem.CONN_STATUS_ERROR);
			if(blockFurtherCommands)
			{
				outputConsoleComponent.disableInputField();
			}
		}
		else
		{
			MyNotifications.showApplicationError(errorMessage);
		}
	}
}
