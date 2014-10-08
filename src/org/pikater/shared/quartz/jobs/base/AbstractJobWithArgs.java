package org.pikater.shared.quartz.jobs.base;

import org.pikater.shared.quartz.MyJobScheduler;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * <p>Base class for all jobs that allows generic job parameters
 * in {@link MyJobScheduler} and provides a few convenience
 * routines. The framework requires subclasses not to define a zero-arg
 * constructor.</p>
 */
public abstract class AbstractJobWithArgs implements Job
{
	/**
	 * The number of arguments this job accepts.
	 */
	private final int numArgs;
	
	/**
	 * Reference to storeand hide the default context and provide our
	 * own job argument interface.
	 */
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
	
	/**
	 * Gets the argument this job received with the given index.
	 * 
	 * @param index
	 * @return
	 * @throws JobExecutionException
	 */
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
	
	/**
	 * Determines whether the argument has a correct type and 
	 * possibly even content.
	 *  
	 * @param index
	 * @param arg
	 * @return
	 */
	public abstract boolean argumentCorrect(Object argument, int argIndex);
	
	/**
	 * Defines (most importantly) job identity if needed.
	 * @param builder
	 */
	public abstract void buildJob(JobBuilder builder);
	
	/**
	 * Gets the schedule for this job.
	 * @return
	 */
	public abstract Trigger getJobTrigger();
	
	/**
	 * Job execution handler.
	 * 
	 * @throws JobExecutionException
	 */
	protected abstract void execute() throws JobExecutionException;
}