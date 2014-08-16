package org.pikater.web.vaadin.gui.server.ui_visualization.components;

import java.util.UUID;

import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.ImageDownloadResource;
import org.pikater.web.vaadin.gui.server.components.imageviewer.ImageViewer;
import org.pikater.web.vaadin.gui.server.components.popups.MyPopup;
import org.pikater.web.visualisation.definition.result.DSVisOneSubresult;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/** 
 * TODO: This class is only a temporary solution to visualization. Much better
 * (but also much more time-consuming) solution would be to use deep zoom images
 * and a client-side viewer for them (like openseadragon).
 */
@StyleSheet("chartThumbnail.css") class ChartThumbnail extends VerticalLayout
{
	private static final long serialVersionUID = -308511560261756785L;
	
	private final String caption;
	private final UUID resourceID;
	private final String downloadURL;

	public ChartThumbnail(DSVisOneSubresult imageResult, final int imageWidth, final int imageHeight)
	{
		super();
		setStyleName("chartThumbnail");
		setSizeUndefined();
		
		// data source
		this.caption = String.format("%s x %s", imageResult.toLeftIndex(), imageResult.toTopIndex());
		this.resourceID = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), new ImageDownloadResource(
				imageResult.getFile(),
				ResourceExpiration.ON_SESSION_END,
				imageResult.getImageType().toMimeType(),
				imageWidth,
				imageHeight)
		);
		this.downloadURL = ResourceRegistrar.getDownloadURL(this.resourceID);
		
		// UI
		Label lbl_imageResult = new Label(caption);
		Image img_imageResult = new Image(null, new ExternalResource(downloadURL));
		img_imageResult.setSizeUndefined();
		img_imageResult.setDescription("Double click to enlarge.");
		img_imageResult.addClickListener(new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = 5830107000487540250L;

			@Override
			public void click(ClickEvent event)
			{
				if(event.isDoubleClick())
				{
					ImageViewer viewer = new ImageViewer(downloadURL, imageWidth, imageHeight);
					viewer.setSizeFull();
					
					MyPopup popup = new MyPopup(caption, viewer);
					popup.setCaptionCentered();
					popup.setWidth("800px");
					popup.setHeight("800px");
					popup.show();
				}
			}
		});
		
		addComponent(lbl_imageResult);
		addComponent(img_imageResult);
		setExpandRatio(img_imageResult, 1);
	}
}