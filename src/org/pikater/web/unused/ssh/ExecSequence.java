package org.pikater.web.unused.ssh;

import java.util.ArrayList;
import java.util.List;

import org.pikater.web.unused.components.console.SimpleConsoleComponent;

public class ExecSequence
{
	private static final int DEFAULT_STATE = -1;
	
	private final List<String> commands;
	private final boolean stopIfAnErrorIsEncountered;
	private int executionFailedAtCommand;

	public ExecSequence(boolean stopIfAnErrorIsEncountered) 
	{
		this.commands = new ArrayList<String>();
		this.stopIfAnErrorIsEncountered = stopIfAnErrorIsEncountered;
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
		return executionFailedAtCommand == DEFAULT_STATE;
	}
	
	private void resetState()
	{
		executionFailedAtCommand = DEFAULT_STATE;
	}
}
