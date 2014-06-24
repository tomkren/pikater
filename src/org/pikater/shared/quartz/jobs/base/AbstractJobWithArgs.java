package org.pikater.shared.quartz.jobs.base;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * A general implementation to allow more or less generic job scheduling and provide a few routines.
 * Child classes are required not to define a non-default (zero-arg) constructor by the framework.
 */
public abstract class AbstractJobWithArgs implements Job
{
	private final int numArgs;
	private JobExecutionContext executionContext;
	
	public AbstractJobWithArgs(int numArgs)
	{
		if(numArgs < 0)
		{
			throw new IllegalArgumentException("Number of arguments can not be a negative number.");
		}
		this.numArgs = numArgs;
		this.executionContext = null;
	}
	
	public int getNumberOfArguments()
	{
		return numArgs;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Object> T getArg(int index) throws JobExecutionException
	{
		if((index >= 0) && index < numArgs)
		{
			if(executionContext != null)
			{
				Object result = executionContext.getJobDetail().getJobDataMap().get(String.valueOf(index));
				try
				{
					return (T) result;
				}
				catch (ClassCastException e)
				{
					throw new JobExecutionException(String.format("Argument number %d is of type '%s' and can not be cast to the type you provided. "
							+ "Recheck your bindings.", index, result.getClass().getName()));
				}
			}
			else
			{
				throw new IllegalStateException("This job has not been executed yet. Only use this method AFTER that.");
			}
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		this.executionContext = arg0;
		if(numArgs != this.executionContext.getJobDetail().getJobDataMap().size())
		{
			throw new JobExecutionException("Arguments have not been set.");
		}
		else
		{
			execute();
		}
	}
	
	// --------------------------------------------------
	// ABSTRACT INTERFACE
	
	public abstract boolean argumentCorrect(int index, Object arg);
	public abstract void buildJob(JobBuilder builder);
	public abstract Trigger getJobTrigger();
	protected abstract void execute() throws JobExecutionException;
}