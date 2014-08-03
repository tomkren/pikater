package org.pikater.web.vaadin.gui.server.ui_visualization.components;

import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisTwoUIArgs;

import com.vaadin.ui.VerticalLayout;

public class CompareDatasetsVisualizer extends VerticalLayout
{
	private static final long serialVersionUID = 3682122092659178186L;

	public CompareDatasetsVisualizer(DSVisTwoUIArgs arguments)
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