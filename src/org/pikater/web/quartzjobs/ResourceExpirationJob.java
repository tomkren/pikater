package org.pikater.web.quartzjobs;

import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.DateBuilder.*;

import java.util.UUID;

import org.pikater.shared.quartz.jobs.base.AbstractJobWithArgs;
import org.pikater.web.servlets.DynamicDownloadServlet;
import org.pikater.web.sharedresources.IRegistrarResource;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class ResourceExpirationJob extends AbstractJobWithArgs
{
	public ResourceExpirationJob()
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
				return arg instanceof IRegistrarResource;
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
		IRegistrarResource resource = getArg(1);
		ResourceRegistrar.expireOnFirstPickupResource(token, resource);
	}
}