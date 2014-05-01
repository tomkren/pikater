package org.pikater.web.vaadin.gui;

public class ExecSyncResult
{
	public final String response;
	private final boolean commandExecutedSuccessfully;

	public ExecSyncResult(String response, boolean commandExecutedSuccessfully)
	{
		this.response = response;
		this.commandExecutedSuccessfully = commandExecutedSuccessfully;
	}
	
	public boolean commandWasExecuted()
	{
		return commandExecutedSuccessfully;
	}
}
