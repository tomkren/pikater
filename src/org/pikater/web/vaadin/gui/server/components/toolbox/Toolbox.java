package org.pikater.web.vaadin.gui.server.components.toolbox;

import org.pikater.web.vaadin.MyResources;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("toolbox.css")
public class Toolbox extends CustomComponent
{
	private static final long serialVersionUID = 5925115342286749639L;
	
	public Toolbox(String caption, Component content, MouseEvents.ClickListener minimizeAction)
	{
		super();
		setSizeFull();
		
		Image bar_minimizeIcon = new Image(null, MyResources.img_minimizeIcon16);
		bar_minimizeIcon.setStyleName("toolbox-bar-buttons-min");
		bar_minimizeIcon.addClickListener(minimizeAction);
		
		Label bar_label = new Label(caption);
		bar_label.setStyleName("toolbox-bar-caption-label");
		
		CustomLayout bar = new CustomLayout("toolboxBar");
		bar.addComponent(bar_label, "caption");
		bar.addComponent(bar_minimizeIcon, "buttons");
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setSpacing(false);
		mainLayout.addComponent(bar);
		// mainLayout.addComponent(content);
		// mainLayout.setExpandRatio(content, 1);
		
		setCompositionRoot(mainLayout);
	}
}