package org.pikater.web.quartzjobs.results;

import java.io.File;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class ExportExperimentResultsJob extends InterruptibleImmediateOneTimeJob
{
	public ExportExperimentResultsJob()
	{
		super(3);
	}

	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch(index)
		{
			case 0:
				return arg instanceof JPAExperiment;
			case 1:
				return arg instanceof File;
			case 2:
				return arg instanceof IProgressDialogResultHandler;
				
			default:
				return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder)
	{
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		// information from GUI
		JPAExperiment experiment = getArg(0);
		File resultFile = getArg(1);
		IProgressDialogResultHandler resultHandler = getArg(2);

		// the actual action
		try
		{
			float resultsExported = 0;
			float resultCount = experiment.getResults().size();  
			for(JPAResult result : experiment.getResults())
			{
				// TODO: export result to the above file
				
				resultsExported++;
				resultHandler.updateProgress(resultsExported / resultCount);
			}
			resultHandler.finished(null);
		}
		catch (Throwable t)
		{
			PikaterLogger.logThrowable("Job could not finish because of the following error:", t);
			resultHandler.failed(); // don't forget to... important cleanup will take place
		}
		finally
		{
			// generated temporary files will be deleted when the JVM exits
		}
	}
}