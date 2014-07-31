package org.pikater.web.quartzjobs.visualization;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrComparisons;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetComparisonResult;
import org.pikater.web.visualisation.definition.task.IVisualizeDatasetComparison;
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
				// TODO:
				
				/*
				File file = new File((String) getArg(3));
				
				PrintStream output=new PrintStream(file);
				Object attrArg1=getArg(4);
				Object attrArg2=getArg(5);
				Object attrArg3=getArg(6);
				Object attrArg4=getArg(7);
				Object attrArg5=getArg(8);
				Object attrArg6=getArg(9);

				ComparisonSVGGenerator csvgg;
				if(
						(attrArg1 instanceof String)&&(attrArg2 instanceof String)&&(attrArg3 instanceof String)&&
						(attrArg4 instanceof String)&&(attrArg5 instanceof String)&&(attrArg6 instanceof String)
						)
				{
					csvgg=new ComparisonSVGGenerator(listener, output,dslo1,dslo2, (String)attrArg1,(String)attrArg2, (String)attrArg3,(String)attrArg4,(String)attrArg5, (String)attrArg6);
				}else if(
						(attrArg1 instanceof Integer)&&(attrArg2 instanceof Integer)&&(attrArg3 instanceof Integer)&&
						(attrArg4 instanceof Integer)&&(attrArg5 instanceof Integer)&&(attrArg6 instanceof Integer)
						)
				{
					csvgg=new ComparisonSVGGenerator(listener, output,dslo1,dslo2, (Integer)attrArg1,(Integer)attrArg2, (Integer)attrArg3,(Integer)attrArg4,(Integer)attrArg5, (Integer)attrArg6);
				}else{
					output.close();
					listener.failed();
					return;
				}

				csvgg.create();
				result.finished();
				*/
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
