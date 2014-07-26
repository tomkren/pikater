package org.pikater.web.vaadin.gui.server.components.imageviewer;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.vaadin.peter.imagescaler.ImageScaler;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * A image viewer for large images capable of zooming in and out.</br>
 * TODO: This class is only a temporary solution to visualization. Much better
 * (but also much more time-consuming) solution would be to use deep zoom images
 * and a client-side viewer for them (like openseadragon).
 */
@StyleSheet("imageViewer.css")
public class ImageViewer extends VerticalLayout
{
	private static final long serialVersionUID = 1080049714636026620L;
	
	private final ImageScaler scaler;
	private int currentZoomLevel;
	
	public ImageViewer(String imageURL, final int imageWidth, final int imageHeight)
	{
		super();
		setPrimaryStyleName("imageViewer");
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeUndefined();
		toolbar.setSpacing(true);
		toolbar.addComponent(new Button("Zoom in", new Button.ClickListener()
		{
			private static final long serialVersionUID = 821012519249414375L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				scaler.setWidth(String.valueOf(imageWidth + 500) + "px");
			}
		}));
		toolbar.addComponent(new Button("Zoom out", new Button.ClickListener()
		{
			private static final long serialVersionUID = 821012519249414375L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				// TODO Auto-generated method stub
			}
		}));
		toolbar.addComponent(new Button("Display original image size", new Button.ClickListener()
		{
			private static final long serialVersionUID = 821012519249414375L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				GeneralDialogs.info("Original size", "Width: " + scaler.getImageWidth() + "</br> Height: " + scaler.getImageHeight());
			}
		}));
		
		Panel toolbarContainer = new Panel();
		toolbarContainer.setWidth("100%");
		toolbarContainer.setStyleName("imageViewer-toolbar");
		toolbarContainer.setContent(toolbar);
		
		this.scaler = new ImageScaler();
		this.scaler.setImage(new ExternalResource(imageURL), imageWidth, imageHeight);
		this.scaler.setWidth(String.valueOf(imageWidth) + "px");
		this.scaler.setHeight(String.valueOf(imageHeight) + "px");
		this.scaler.setRecalculateOnSizeChangeEnabled(true);
		
		addComponent(toolbarContainer);
		addComponent(this.scaler);
		setExpandRatio(this.scaler, 1);
		
		this.currentZoomLevel = 1;
	}
	
	public void setImageZoom(int positiveOrNegativePercentageOfOriginalSize)
	{
		// this.scaler.getImageHeight()
	}
}