package org.pikater.web.quartzjobs;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.UUID;

import org.pikater.shared.quartz.jobs.base.AbstractJobWithArgs;
import org.pikater.web.servlets.download.DynamicDownloadServlet;
import org.pikater.web.servlets.download.IDownloadResource;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class DownloadTokenExpirationjob extends AbstractJobWithArgs
{
	public DownloadTokenExpirationjob()
	{
		super(2);
	}

	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch(index)
		{
			case 0:
				return arg instanceof UUID;
			case 1:
				return arg instanceof IDownloadResource;
			default:
				return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder)
	{
	}
	
	@Override
	public Trigger getJobTrigger()
	{
		return newTrigger()
				.withSchedule(
		        		simpleSchedule()
		                .withIntervalInSeconds(DynamicDownloadServlet.EXPIRATION_TIME_IN_SECONDS))
		        .build();
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		UUID token = getArg(0);
		IDownloadResource resource = getArg(1);
		DynamicDownloadServlet.downloadExpired(token, resource);
	}
}