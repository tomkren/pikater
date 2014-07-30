package org.pikater.web.vaadin.gui.server.ui_visualization;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.ImageDownloadResource;
import org.pikater.web.vaadin.gui.server.components.imageviewer.ImageViewer;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.visualisation.DatasetVisualizationEntryPoint;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetResult;
import org.pikater.web.visualisation.definition.result.VisualizeDatasetSubresult;

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
		// then display progress dialog
		ProgressDialog.show("Vizualization progress...", new ProgressDialog.IProgressDialogTaskHandler()
		{
			private DatasetVisualizationEntryPoint underlyingTask;
			
			@Override
			public void startTask(IProgressDialogResultHandler contextForTask) throws Throwable
			{
				// start the task and bind it with the progress dialog
				underlyingTask = new DatasetVisualizationEntryPoint(contextForTask);
				underlyingTask.visualizeDataset(datasetToBeVisualized, null, null); // TODO:
			}
			
			@Override
			public void abortTask()
			{
				underlyingTask.abortVisualization();
			}
			
			@Override
			public void onTaskFinish(IProgressDialogTaskResult result)
			{
				VisualizeDatasetResult visTaskResult = (VisualizeDatasetResult) result;
				for(VisualizeDatasetSubresult singleImageResult : visTaskResult)
				{
					// TODO: do not actually try to display this... 
					
					ImageDownloadResource resource = new ImageDownloadResource(
							singleImageResult.getFile(),
							ResourceExpiration.ON_SESSION_END,
							singleImageResult.getImageType().toMimeType(),
							singleImageResult.getImageWidth(),
							singleImageResult.getImageHeight()
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
			}
		});
	}
}
