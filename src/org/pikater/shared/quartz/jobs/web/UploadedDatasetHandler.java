package org.pikater.shared.quartz.jobs.web;

import java.io.File;

import org.pikater.shared.quartz.jobs.base.ImmediateOneTimeJob;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class UploadedDatasetHandler extends ImmediateOneTimeJob
{
	public UploadedDatasetHandler()
	{
		super(2); // number of arguments
	}
	
	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch (index)
		{
			case 0:
				return arg instanceof File;
			case 1:
				return arg instanceof String;
			default:
				return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder)
	{
		// builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		File uploadedFile = getArg(0); // TODO: don't forget to delete this file when we're done
		String optionalARFFHeaders = getArg(1);
		
		// TODO: implement me
	}
}