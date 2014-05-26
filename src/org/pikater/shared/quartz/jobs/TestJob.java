package org.pikater.shared.quartz.jobs;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobBuilder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class TestJob implements IPikaterJob
{
	@Override
	public void buildJob(JobBuilder builder)
	{
		builder.withIdentity("TestJob", "Jobs");
	}

	@Override
	public Trigger getJobTrigger()
	{
	    return newTrigger()
	        .withIdentity("TestJobTrigger", "Triggers")
	        .startNow()
	        .withSchedule(
	        		simpleSchedule()
	                .withIntervalInSeconds(5)
	                .repeatForever())
	        .build();
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		System.out.println("Test job fired!");
	}
}
