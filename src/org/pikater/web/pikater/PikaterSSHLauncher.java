package org.pikater.web.pikater;

import org.pikater.web.vaadin.gui.ExecSyncResult;
import org.pikater.web.vaadin.gui.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.SimpleConsoleComponent.MessageStyle;
import org.pikater.web.vaadin.gui.welcometour.RemoteServerInfoItem;
import org.pikater.web.vaadin.gui.welcometour.RemoteServerInfoItem.Header;
import org.pikater.shared.AppHelper;
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
			
			// first change the working directory if need be
			String directory = serverInfo.underlyingInfoInstance.getField(FieldType.DIRECTORY);
			if(!FieldVerifier.isStringNullOrEmpty(directory))
			{
				// try to change the directory and see what happens
				ExecSyncResult result = this.outputConsoleComponent.execSync(
						String.format("cd \"%s\"", serverInfo.underlyingInfoInstance.getField(FieldType.DIRECTORY)));
				if(!result.commandWasExecuted() || !FieldVerifier.isStringNullOrEmpty(result.response)) // cd commands produce no output if no errors occur
				{
					handleError("Could not navigate to the predetermined directory.", false);
				}
			}
			
			// now launch pikater
			switch (serverInfo.getServerType())
			{
				case MASTER:
					// return launchPikaterInMasterMode();
					return false;
				case SLAVE:
					// return launchPikaterInSlaveMode();
					return false;
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
		// TODO: tady si to napis, pouzij "remoteExec.execSync" a "remoteExec.execAsync" - detaily najdes v Javadocu
		
		// predpripravenej prectenej skript
		String command = AppHelper.readTextFile(AppHelper.joinPathComponents(AppHelper.corePath, "runPikaterMaster.sh"));
		

		boolean launchedSuccessfully = false;

		this.outputConsoleComponent.execAsync("cd BIG/Softwerak");
		this.outputConsoleComponent.execAsync("git clone https://github.com/krajj7/pikater");
		this.outputConsoleComponent.execAsync("cd pikater");
		this.outputConsoleComponent.execAsync("cd core");
		this.outputConsoleComponent.execAsync("./buildPikaterCore.sh");
		//this.outputConsoleComponent.execAsync("./runPikaterCoreMaster.sh");
		
		
		// TODO: az to dodelas, nastav zaznamu v tabulce spravnej stav:
		// updateConnectionStatus(RemoteServerInfoItem.connectionStatus_launched); // vsechno probehlo v poradku
		// updateConnectionStatus(RemoteServerInfoItem.connectionStatus_error); // behem spousteni se vyskytla chyba
		
		// TODO: kdybys chtel napsat do konzole nejakou vlastni chybovou zpravu, pouzij:
		// handleError("tvoje zprava", false); // jestli tohle pouzijes, nemusis nastavovat zaznamu v tabulce chybu (viz predchozi TODO) - dela se to automaticky
		
		return launchedSuccessfully;
	}
	
	private boolean launchPikaterInSlaveMode()
	{
		int currentSlaveID = getNextSlaveID(); // TODO: staci jenom pouzit, nic vic neni treba - inkrementuje se automaticky
		
		// predpripravenej prectenej skript
		String command = AppHelper.readTextFile(AppHelper.joinPathComponents(AppHelper.corePath, "runPikaterSlave.sh"));
		
		// TODO: tady si to napis, pouzij "remoteExec.execSync" a "remoteExec.execAsync" - detaily najdes v Javadocu
		
		boolean launchedSuccessfully = false;
		
		// TODO: az to dodelas, nastav zaznamu v tabulce spravnej stav:
		// updateConnectionStatus(RemoteServerInfoItem.connectionStatus_launched); // vsechno probehlo v poradku
		// updateConnectionStatus(RemoteServerInfoItem.connectionStatus_error); // behem spousteni se vyskytla chyba
		
		// TODO: kdybys chtel napsat do konzole nejakou vlastni chybovou zpravu, pouzij:
		// handleError("tvoje zprava", false); // jestli tohle pouzijes, nemusis nastavovat zaznamu v tabulce chybu (viz predchozi TODO) - dela se to automaticky
				
		return launchedSuccessfully;
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
