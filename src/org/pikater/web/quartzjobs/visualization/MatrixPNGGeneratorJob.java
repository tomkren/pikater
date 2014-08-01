package org.pikater.web.quartzjobs.visualization;

import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetResult;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetSubresult;
import org.pikater.web.visualisation.definition.task.IVisualizeDataset;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SinglePNGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class MatrixPNGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IVisualizeDataset
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
		// TODO Auto-generated method stub
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
		VisualizeDatasetResult result = new VisualizeDatasetResult(context);
		try
		{
			int count=0;
			for(String attrY : attrs)
			{
				for(String attrX : attrs)
				{
					/*
					 * TODO:
					 * 1) perhaps a static field to denote which extension to use by default?
					 * 2) ChartGenerator.MATRIX_CHART_SIZE is now obsolete
					 * 3) If you wish, we can propagate the result type into your own code
					 * where you could use setters.  
					 */			
					/*
					 * TODO: is this version OK? we retrieve dataset from DB multiple times (for each subchart)
					 *       but we only store one bitmap in memory for each cycle 
					 **/
					
					VisualizeDatasetSubresult imageResult = result.createSingleImageResult(
							new AttrMapping(attrX, attrY, attrTarget),
							ImageType.PNG,
							ChartGenerator.SINGLE_CHART_SIZE,
							ChartGenerator.SINGLE_CHART_SIZE
					);
					PrintStream output = new PrintStream(imageResult.getFile());
					//TODO: progress update for multiple image tiles
					new SinglePNGGenerator(null, dataset, output, attrX, attrY,attrs[attrs.length-1]).create();
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