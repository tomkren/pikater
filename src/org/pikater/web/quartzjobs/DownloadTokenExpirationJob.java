package org.pikater.web.quartzjobs;

import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.DateBuilder.*;

import java.util.UUID;

import org.pikater.shared.quartz.jobs.base.AbstractJobWithArgs;
import org.pikater.web.servlets.download.DownloadRegistrar;
import org.pikater.web.servlets.download.DynamicDownloadServlet;
import org.pikater.web.servlets.download.resources.IDownloadResource;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class DownloadTokenExpirationJob extends AbstractJobWithArgs
{
	public DownloadTokenExpirationJob()
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
				.startAt(futureDate(DynamicDownloadServlet.EXPIRATION_TIME_IN_SECONDS, IntervalUnit.SECOND))
		        .build();
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		UUID token = getArg(0);
		IDownloadResource resource = getArg(1);
		DownloadRegistrar.downloadExpired(token, resource);
	}
}