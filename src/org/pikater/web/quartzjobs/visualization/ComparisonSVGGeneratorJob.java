package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.pglargeobject.PostgreLobAccess;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.shared.util.Tuple;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrComparisons;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;
import org.pikater.web.visualisation.definition.result.DSVisTwoResult;
import org.pikater.web.visualisation.definition.result.DSVisTwoSubresult;
import org.pikater.web.visualisation.definition.task.IDSVisTwo;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.ComparisonSVGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class ComparisonSVGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IDSVisTwo
{
	private IProgressDialogResultHandler context;
	
	public ComparisonSVGGeneratorJob()
	{
		super(4);
	}

	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch(index)
		{
			case 0:
			case 1:
				return arg instanceof JPADataSetLO;
			case 2:
				return arg instanceof AttrComparisons;
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
		JPADataSetLO dataset1 = getArg(0);
		JPADataSetLO dataset2 = getArg(1);
		AttrComparisons comparisonList = getArg(2);
		context = getArg(3);
		visualizeDatasetComparison(dataset1, dataset2, comparisonList);
	}

	@Override
	public void visualizeDatasetComparison(JPADataSetLO dataset1, JPADataSetLO dataset2, AttrComparisons comparisonList)
	{
		DSVisTwoResult result = new DSVisTwoResult(context, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
		try
		{
			if(dataset1.getHash() == dataset2.getHash())
			{
				throw new IllegalArgumentException("Identical datasets were received for comparison.");
			}
			else
			{
				// TODO: check whether metadata are computed: exception or compute now?
				// TODO: yes, throw exception for sure :)
				
				File datasetCachedFile1 = PostgreLobAccess.downloadFileFromDB(dataset1.getOID());
				File datasetCachedFile2=PostgreLobAccess.downloadFileFromDB(dataset2.getOID());
				
				int count=0;
				for(Tuple<AttrMapping, AttrMapping> attrsToCompare : comparisonList)
				{
					DSVisTwoSubresult imageResult = result.createSingleImageResult(attrsToCompare, ImageType.SVG);
					new ComparisonSVGGenerator(
							null, // no need to pass in progress listener - progress is updated below
							new PrintStream(imageResult.getFile()),
							dataset1,
							dataset2,
							datasetCachedFile1,
							datasetCachedFile2,
							attrsToCompare.getValue1().getAttrX(),
							attrsToCompare.getValue2().getAttrX(),
							attrsToCompare.getValue1().getAttrY(),
							attrsToCompare.getValue2().getAttrY(),
							attrsToCompare.getValue1().getAttrTarget(),
							attrsToCompare.getValue2().getAttrTarget()
							).create();
					count++;
					result.updateProgress(100*count/comparisonList.size());
				}
				result.finished();
			}
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
