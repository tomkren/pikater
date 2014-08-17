package org.pikater.web.vaadin.gui.server.layouts.matrixlayout.thumbnail;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/** 
 * TODO: This class is only a temporary solution to visualization. An alternative
 * (but much more time-consuming) solution would be to use deep zoom images
 * and a client-side viewer for them (like openseadragon).
 */
@StyleSheet("thumbnail.css")
public abstract class Thumbnail extends VerticalLayout
{
	private static final long serialVersionUID = -308511560261756785L;
	
	public Thumbnail()
	{
		super();
		setSizeUndefined();
		setStyleName("thumbnail");
	}
	
	protected void init()
	{
		Component content = getContent();
		content.setSizeUndefined();
		content.addStyleName("itemContent");
		
		addComponent(new Label(getContentCaption()));
		addComponent(content);
		setExpandRatio(content, 1);
	}
	
	protected abstract String getContentCaption();
	protected abstract Component getContent();
}