package org.pikater.web.vaadin.gui.server.ui_visualization;

import org.pikater.web.vaadin.CustomConfiguredUI;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.VaadinRequest;

@JavaScript(value = "openseadragon.min.js")
public class VisualizationUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -8917289148464357783L;

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Don't forget to call this.
		 * IMPORTANT:
		 * 1) You shouldn't update the UI in this method. You only provide the content component
		 * when you're asked to in the {@link #displayChildContent()} method.
		 * 2) When {@link #displayChildContent()} is called, this method is still not finished.
		 * You shouldn't have any initializing code after the super.init() all.
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
		
		// TODO: wrap the image in a decent JS or jQuery image viewer and make a new UI for the visualization result
	}
}