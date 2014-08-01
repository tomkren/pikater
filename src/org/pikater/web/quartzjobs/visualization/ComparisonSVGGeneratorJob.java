package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.shared.util.Tuple;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrComparisons;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetComparisonResult;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetSubresult;
import org.pikater.web.visualisation.definition.task.IVisualizeDatasetComparison;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.ComparisonSVGGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SinglePNGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class ComparisonSVGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IVisualizeDatasetComparison
{
	private IProgressDialogResultHandler context;
	
	public ComparisonSVGGeneratorJob()
	{
		super(10);
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
		AttrComparisons attrsToCompare = getArg(2);
		context = getArg(3);
		visualizeDatasetComparison(dataset1, dataset2, attrsToCompare);
	}

	@Override
	public void visualizeDatasetComparison(JPADataSetLO dataset1, JPADataSetLO dataset2, AttrComparisons attrsToCompare)
	{
		VisualizeDatasetComparisonResult result = new VisualizeDatasetComparisonResult(context);
		try
		{
			if(dataset1.getHash() == dataset2.getHash())
			{
				throw new IllegalArgumentException("Identical datasets were received for comparison.");
			}
			else
			{
				int count=0;
				for(Tuple<AttrMapping, AttrMapping> tuple : attrsToCompare){
					
					/**
					 * TODO: image results for comparison
					 
					VisualizeDatasetSubresult imageResult = result.createSingleImageResult(
							new AttrMapping(tuple.getValue1().getAttrX(), tuple.getValue1().getAttrY(), tuple.getValue1().getAttrTarget()),
							ImageType.SVG,
							ChartGenerator.SINGLE_CHART_SIZE,
							ChartGenerator.SINGLE_CHART_SIZE
					);
					PrintStream output = new PrintStream(imageResult.getFile());
					**/
					
					//just for compilation without errors
					PrintStream output = new PrintStream(new File("core/datasets/visual"));
					
					//TODO: progress update for multiple image tiles
					new ComparisonSVGGenerator(
							null,
							output,
							dataset1,
							dataset2,
							tuple.getValue1().getAttrX(),
							tuple.getValue2().getAttrX(),
							tuple.getValue1().getAttrY(),
							tuple.getValue2().getAttrY(),
							tuple.getValue1().getAttrTarget(),
							tuple.getValue2().getAttrTarget()
							).create();
					
					count++;
					result.updateProgress(100*count/attrsToCompare.size());
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
