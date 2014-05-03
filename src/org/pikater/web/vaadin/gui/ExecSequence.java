package org.pikater.web.vaadin.gui;

import java.util.ArrayList;
import java.util.List;

public class ExecSequence
{
	private static final int default_state = -1;
	
	private final List<String> commands;
	private final boolean stopIfAnErrorIsEncountered;
	private int executionFailedAtCommand;

	public ExecSequence(boolean stopIfAnErrorIsEncountered) 
	{
		this.commands = new ArrayList<String>();
		this.stopIfAnErrorIsEncountered = stopIfAnErrorIsEncountered;;
		resetState();
	}
	
	public void enqueueCommand(String command)
	{
		if(command == null)
		{
			throw new NullPointerException();
		}
		else
		{
			this.commands.add(command);
		}
	}
	
	public boolean exec(SimpleConsoleComponent console)
	{
		resetState();
		for(String command : commands)
		{
			if(!console.execAsync(command))
			{
				executionFailedAtCommand = this.commands.indexOf(command);
				if(stopIfAnErrorIsEncountered)
				{
					break;
				}
			}
		}
		return isStateDefault();
	}
	
	public String getFailedCommand()
	{
		if(!isStateDefault())
		{
			return commands.get(executionFailedAtCommand);
		}
		else
		{
			return null;
		}
	}
	
	private boolean isStateDefault()
	{
		return executionFailedAtCommand == default_state;
	}
	
	private void resetState()
	{
		executionFailedAtCommand = default_state;
	}
}
