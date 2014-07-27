package org.pikater.web.vaadin.gui.server.ui_visualization;

import java.io.File;
import java.util.UUID;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.util.IOUtils;
import org.pikater.web.quartzjobs.visualization.MatrixPNGGeneratorJob;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.ImageDownloadResource;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskContext;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.ProgressDialogVisualizationTaskResult;
import org.quartz.JobKey;

import com.google.common.net.MediaType;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class CompareDatasetsVisualizer extends VerticalLayout
{

	public CompareDatasetsVisualizer(JPADataSetLO dataset1, JPADataSetLO dataset2)
	{
		super();
		
	}
	
	private void generateDatasetComparison()
	{
		/*
		// create an image file to which the visualization module will generate the image
		final File tmpFile = IOUtils.createTemporaryFile("visualization-generated", ".png");
		
		// then display progress dialog
		ProgressDialog.show("Vizualization progress...", new IProgressDialogHandler()
		{
			private JobKey jobKey = null;
			
			@Override
			public void startTask(IProgressDialogTaskContext context) throws Throwable
			{
				// start the task and bind it with the progress dialog
				jobKey = PikaterJobScheduler.getJobScheduler().defineJob(MatrixPNGGeneratorJob.class, new Object[]
				{
					context,
					arguments.getDatasetToBeViewed(),
					tmpFile.getAbsolutePath()
				});
			}
			
			@Override
			public void abortTask()
			{
				if(jobKey == null)
				{
					PikaterLogger.logThrowable("", new NullPointerException("Can not abort a task that has not started."));
				}
				else
				{
					try
					{
						PikaterJobScheduler.getJobScheduler().interruptJob(jobKey);
					}
					catch (Throwable t)
					{
						PikaterLogger.logThrowable(String.format("Could not interrupt job: '%s'. What now?", jobKey.toString()), t);
					}
				}
			}
			
			@Override
			public void onTaskFinish(IProgressDialogTaskResult result)
			{
				ProgressDialogVisualizationTaskResult visTaskResult = (ProgressDialogVisualizationTaskResult) result;
								
				// TODO:
			}
		});
		*/
	}
}