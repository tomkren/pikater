package org.pikater.shared.quartz.jobs.crons;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.pikater.shared.quartz.jobs.base.ZeroArgJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

@DisallowConcurrentExecution
public class RemoveExpiredTrainedModels extends ZeroArgJob
{
	@Override
	public void buildJob(JobBuilder builder)
	{
		// builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	public Trigger getJobTrigger()
	{
	    return newTrigger()
	        // .withIdentity("TestJobTrigger", "Triggers")
	        .startNow()
	        .withSchedule(
	        		simpleSchedule()
	                .withIntervalInHours(24 * 7) // 1 week
	                .repeatForever())
	        .build();
	}
	
	@Override
	public void execute() throws JobExecutionException
	{
		// TODO: implement
	}
}