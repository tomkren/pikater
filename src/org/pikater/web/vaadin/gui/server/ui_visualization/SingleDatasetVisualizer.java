package org.pikater.web.vaadin.gui.server.ui_visualization;

import java.io.File;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.util.IOUtils;
import org.pikater.web.quartzjobs.visualization.MatrixPNGGeneratorJob;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.ImageDownloadResource;
import org.pikater.web.vaadin.gui.server.components.imageviewer.ImageViewer;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskContext;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.ProgressDialogVisualizationTaskResult;
import org.quartz.JobKey;

import com.google.common.net.MediaType;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class SingleDatasetVisualizer extends VerticalLayout
{
	private static final long serialVersionUID = 8665905099485047156L;
	
	private final JPADataSetLO datasetToBeVisualized;
	
	public SingleDatasetVisualizer(JPADataSetLO datasetToBeVisualized)
	{
		super();
		
		this.datasetToBeVisualized = datasetToBeVisualized;
		
		generateImageMatrix();
	}
	
	private void generateImageMatrix()
	{
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
					datasetToBeVisualized,
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
				ImageDownloadResource resource = new ImageDownloadResource(
						tmpFile,
						ResourceExpiration.ON_SESSION_END,
						MediaType.PNG.toString(),
						visTaskResult.getImageWidth(),
						visTaskResult.getImageHeight()
				);
				ImageViewer viewer = new ImageViewer(
						ResourceRegistrar.getDownloadURL(ResourceRegistrar.registerResource(VaadinSession.getCurrent(), resource)),
						resource.getImageWidth(),
						resource.getImageHeight()
				);
				viewer.setWidth("800px");
				viewer.setHeight("800px");
				
				addComponent(new Panel(viewer));
			}
		});
	}
}
