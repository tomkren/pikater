package org.pikater.web.vaadin.gui.server.components.imageviewer;

import org.pikater.web.vaadin.gui.server.components.scalableimage.ScalableImage;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

/**
 * A image viewer for large images capable of zooming in and out. 
 */
@StyleSheet("imageViewer.css")
public class ImageViewer extends VerticalLayout
{
	private static final long serialVersionUID = 1080049714636026620L;
	
	private final ScalableImage scaler;
	
	public ImageViewer(String imageURL, int imageWidth, int imageHeight)
	{
		super();
		setPrimaryStyleName("imageViewer");
		
		Label lbl_zoom = new Label("Zoom level (PCT):");
		
		Slider slider = new Slider(0, 2, 2);
		slider.setWidth("200px");
		slider.setImmediate(true);
		slider.addValueChangeListener(new Property.ValueChangeListener()
		{
			private static final long serialVersionUID = 6756349322188831368L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				scaler.setScaleRatio((Double) event.getProperty().getValue());
			}
		});
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSizeUndefined();
		toolbar.setSpacing(true);
		toolbar.addComponent(lbl_zoom);
		toolbar.addComponent(slider);
		toolbar.setComponentAlignment(slider, Alignment.MIDDLE_LEFT);
		
		Panel toolbarContainer = new Panel();
		toolbarContainer.setWidth("100%");
		toolbarContainer.setStyleName("imageViewer-toolbar");
		toolbarContainer.setContent(toolbar);
		
		this.scaler = new ScalableImage(imageURL, imageWidth, imageHeight);
		this.scaler.setSizeFull();
		slider.setValue(new Double(0.5)); // zoom out to half of the image's size
		
		addComponent(toolbarContainer);
		addComponent(this.scaler);
		setExpandRatio(this.scaler, 1);
	}
}