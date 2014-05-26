package org.pikater.shared.quartz.jobs;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class RemoveExpiredTrainedModels implements IPikaterJob
{
	@Override
	public void buildJob(JobBuilder builder)
	{
		builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	public Trigger getJobTrigger()
	{
	    return newTrigger()
	        .withIdentity("TestJobTrigger", "Triggers")
	        .startNow()
	        .withSchedule(
	        		simpleSchedule()
	                .withIntervalInHours(24 * 7) // 1 week
	                .repeatForever())
	        .build();
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		// TODO: implement
	}
}