package org.pikater.shared.quartz.jobs;

import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.quartz.JobKey;

public class InterruptibleJobHelper
{
	private JobKey jobKey;
	
	public InterruptibleJobHelper()
	{
		this.jobKey = null;
	}
	
	public void startJob(Class<? extends InterruptibleImmediateOneTimeJob> jobClass, Object[] jobArgs) throws Exception
	{
		jobKey = PikaterJobScheduler.getJobScheduler().defineJob(jobClass, jobArgs);
	}
	
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