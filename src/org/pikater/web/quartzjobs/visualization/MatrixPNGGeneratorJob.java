package org.pikater.web.quartzjobs.visualization;

import java.io.File;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.postgre.largeobject.IPGLOActionContext;
import org.pikater.shared.database.postgre.largeobject.PGLargeObjectAction;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.InterruptibleImmediateOneTimeJob;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;
import org.pikater.web.visualisation.definition.result.DSVisOneSubresult;
import org.pikater.web.visualisation.definition.task.IDSVisOne;
import org.pikater.web.visualisation.exception.MetadataNotPresentException;
import org.pikater.web.visualisation.implementation.generator.ChartGenerator;
import org.pikater.web.visualisation.implementation.generator.quartz.SinglePNGGenerator;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class MatrixPNGGeneratorJob extends InterruptibleImmediateOneTimeJob implements IDSVisOne, IPGLOActionContext
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
				return arg instanceof JPAAttributeMetaData[];
			case 2:
				return arg instanceof JPAAttributeMetaData;
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
		JPAAttributeMetaData[] attrs = getArg(1);
		JPAAttributeMetaData attrTarget = getArg(2);
		context = getArg(3);
		visualizeDataset(dataset, attrs, attrTarget);
	}
	
	@Override
	public boolean isInterrupted()
	{
		return super.isInterrupted();
	}

	@Override
	public void visualizeDataset(JPADataSetLO dataset, JPAAttributeMetaData[] attrs, JPAAttributeMetaData attrTarget)
	{
		DSVisOneResult result = new DSVisOneResult(context, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
		try
		{
			if((dataset.getGlobalMetaData()==null) || (dataset.getAttributeMetaData()==null))
			{
				throw new MetadataNotPresentException();
			}
			
			File datasetCachedFile = new PGLargeObjectAction(this).downloadLOFromDB(dataset.getOID());
			
			float subresultsGenerated = 0;
			float finalCountOfSubresults = attrs.length * attrs.length;
			for(JPAAttributeMetaData attrY : attrs)
			{
				for(JPAAttributeMetaData attrX : attrs)
				{
					// interrupt generation when the user commands it
					if(isInterrupted())
					{
						return;
					}
					
					// otherwise continue generating
					DSVisOneSubresult imageResult = result.createSingleImageResult(
							new AttrMapping(attrX, attrY, attrTarget),
							ImageType.PNG
					);
					new SinglePNGGenerator(
							null, // no need to pass in progress listener - progress is updated below
							dataset,
							datasetCachedFile,
							new PrintStream(imageResult.getFile()),
							attrX.getName(),
							attrY.getName(),
							attrTarget.getName()).create();
					subresultsGenerated++;
					result.updateProgress(subresultsGenerated / finalCountOfSubresults);
				}
			}
			result.finished();
		}
		catch (InterruptedException e)
		{
			// user interrupted visualization, don't log
			result.failed(); // don't forget to... important cleanup will take place
		}
		catch (Throwable t)
		{
			PikaterLogger.logThrowable("Job could not finish because of the following error:", t);
			result.failed(); // don't forget to... important cleanup will take place
		}
		finally
		{
			// generated temporary files will be deleted when the JVM exits
		}
	}
}