package org.pikater.shared.quartz.jobs;

import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.quartz.JobKey;

/**
 * Utility class encapsulating interruptible jobs and providing
 * convenience interface.
 * 
 * @author SkyCrawl
 */
public class InterruptibleJobHelper
{
	private JobKey jobKey;
	
	public InterruptibleJobHelper()
	{
		this.jobKey = null;
	}
	
	/**
	 * Define and schedule an interruptible job with the given
	 * arguments.
	 * 
	 * @param jobClass
	 * @param jobArgs
	 * @throws Exception
	 */
	public void start(Class<? extends InterruptibleImmediateOneTimeJob> jobClass, Object[] jobArgs) throws Exception
	{
		jobKey = PikaterJobScheduler.getJobScheduler().defineJob(jobClass, jobArgs);
	}
	
	/**
	 * Abort the job defined by {@link #start(Class, Object[])}.
	 * @throws {@link IllegalStateException}
	 */
	public void abort()
	{
		if(jobKey == null)
		{
			throw new IllegalStateException("Can not abort a task that has not started.");
		}
		else
		{
			// InterruptibleImmediateOneTimeJob is assumed here
			// TODO: check if started, unschedule
			try
			{
				PikaterJobScheduler.getJobScheduler().interruptJob(jobKey);
			}
			catch (Exception t)
			{
				PikaterDBLogger.logThrowable(String.format("Could not interrupt job: '%s'. What now?", jobKey.toString()), t);
			}
		}
	}
}