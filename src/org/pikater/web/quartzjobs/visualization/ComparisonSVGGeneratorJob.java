package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.postgre.largeobject.IPGLOActionContext;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectAction;
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
import org.pikater.web.visualisation.exception.MetadataNotPresentException;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.ComparisonSVGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class ComparisonSVGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IDSVisTwo, IPGLOActionContext
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
	public boolean isInterrupted()
	{
		return super.isInterrupted();
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
			else if((dataset1.getGlobalMetaData() == null) || (dataset1.getAttributeMetaData() == null))
			{
				throw new MetadataNotPresentException(dataset1.getDescription());
			}
			else if((dataset2.getGlobalMetaData() == null) || (dataset2.getAttributeMetaData() == null))
			{
				throw new MetadataNotPresentException(dataset2.getDescription());
			}
			
			PGLargeObjectAction downloadAction = new PGLargeObjectAction(this);
			File datasetCachedFile1 = downloadAction.downloadLOFromDB(dataset1.getOID());
			File datasetCachedFile2 = downloadAction.downloadLOFromDB(dataset2.getOID());
				
			float subresultsGenerated = 0;
			float finalCountOfSubresults = comparisonList.size();
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
				subresultsGenerated++;
				result.updateProgress(subresultsGenerated / finalCountOfSubresults);
			}
			result.finished();
		}
		catch (Throwable t)
		{
			PikaterLogger.logThrowable("Job could not finish because of the following error:", t);
			result.failed(); // don't forget to... important cleanup will take place
		}
	}
}