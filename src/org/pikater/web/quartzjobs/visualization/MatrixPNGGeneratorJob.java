package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.definition.result.DSVisOneSubresult;
import org.pikater.web.visualisation.definition.task.IDSVisOne;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SinglePNGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class MatrixPNGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IDSVisOne
{
	private IProgressDialogResultHandler context;
	
	public MatrixPNGGeneratorJob()
	{
		super(4);
	}
	
	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch(index)
		{
			case 0:
				return arg instanceof JPADataSetLO;
			case 1:
				return arg instanceof String[];
			case 2:
				return arg instanceof String;
			case 3:
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
		JPADataSetLO dataset = getArg(0);
		String[] attrs = getArg(1);
		String attrTarget = getArg(2);
		context = getArg(3);
		visualizeDataset(dataset, attrs, attrTarget);
	}

	@Override
	public void visualizeDataset(JPADataSetLO dataset, String[] attrs, String attrTarget)
	{
		DSVisOneResult result = new DSVisOneResult(context, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
		try
		{
			// TODO: check whether metadata are computed: exception or compute now?
			// TODO: yes, throw exception for sure :)
			
			File datasetCachedFile = PostgreLobAccess.downloadFileFromDB(dataset.getOID());
			
			int count = 0;
			for(String attrY : attrs)
			{
				for(String attrX : attrs)
				{
					DSVisOneSubresult imageResult = result.createSingleImageResult(
							new AttrMapping(attrX, attrY, attrTarget),
							ImageType.PNG
					);
					new SinglePNGGenerator(
							null, // no need to pass in progress listener - progress is updated below
							dataset,
							datasetCachedFile,
							new PrintStream(imageResult.getFile()),
							attrX,
							attrY,
							attrTarget).create();
					count++;
					result.updateProgress(100*count/attrs.length/attrs.length);
				}
			}
			result.finished();
		}
		catch (Throwable t)
		{
			PikaterLogger.logThrowable("Job could not finish because of the following error:", t);
			result.failed();
		}
	}
	
	@Override
	public void interrupt() throws UnableToInterruptJobException
	{
		/*
		 * TODO: test whether Quartz automatically interrupts the jobs' threads
		 * or we must do it ourselves.
		 */
	}
}