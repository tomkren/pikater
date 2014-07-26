package org.pikater.web.vaadin.gui.server.ui_visualization;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.servlets.download.DownloadRegistrar;
import org.pikater.web.servlets.download.resources.ImageDownloadResource;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet.PikaterUI;
import org.pikater.web.vaadin.gui.server.components.imageviewer.ImageViewer;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.communication.PushMode;

@Title("Visualization")
@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class VisualizationUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -8917289148464357783L;
	private static final String matrixImageResourceParamName = "img";
	
	private UUID matrixImageResourceID = null;

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * First parse and check needed arguments and if they're not correct, return 404.
		 */
		try
		{
			matrixImageResourceID = DownloadRegistrar.exportDownloadTokenToID(request.getParameter(matrixImageResourceParamName));
		}
		catch(Throwable t)
		{
			return404();
		}
		
		boolean resourceValid = (matrixImageResourceID != null) && 
				DownloadRegistrar.isResourceRegistered(matrixImageResourceID) &&
				DownloadRegistrar.getResource(matrixImageResourceID) instanceof ImageDownloadResource;
		if(!resourceValid)
		{
			return404();
		}
		
		/*
		 * Don't forget to call this.
		 * IMPORTANT:
		 * 1) You shouldn't update the UI in this method. You only provide the content component
		 * when you're asked to in the {@link #displayChildContent()} method.
		 * 2) When {@link #displayChildContent()} is called, this method is still not finished.
		 * You shouldn't have any initializing code after the super.init() call.
		 */
		super.init(request);
	}

	@Override
	protected void displayChildContent()
	{
		// Image generatedImgComponent = new Image("Matrix:", new ExternalResource(url));
		
		/*
		addComponent(new Button("Increase size", new Button.ClickListener()
		{
			private static final long serialVersionUID = 5201209851874654711L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				scaler.setWidth("900px");
				scaler.setHeight("900px");
			}
		}));
		*/
		
		ImageDownloadResource resource = (ImageDownloadResource) DownloadRegistrar.getResource(matrixImageResourceID);
		setContent(new ImageViewer(
				DownloadRegistrar.getDownloadURL(matrixImageResourceID),
				resource.getImageWidth(),
				resource.getImageHeight()
		));
	}
	
	public static String getRedirectURLForResourceID(UUID resourceID)
	{
		return CustomConfiguredUI.getRedirectURLToUI(PikaterUI.DATASET_VISUALIZATION) +
				String.format("?%s=%s", matrixImageResourceParamName, DownloadRegistrar.exportIDToDownloadToken(resourceID));
	}
	
	private void return404()
	{
		try
		{
			VaadinServletService.getCurrentResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable("An undefined download token was received "
					+ "but writing an error code of 404 (NOT_FOUND) to the response failed because of the "
					+ "following exception. Vaadin should have defaulted to error code 500 instead.", e);
		}
	}
}