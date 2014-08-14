package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.customtabsheet;

import org.pikater.web.sharedresources.ThemeResources;
import org.pikater.web.vaadin.gui.server.components.iconbutton.IconButton;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class TabSheetTabComponent extends Panel // something clickable
{
	private static final long serialVersionUID = -8893352392192406060L;
	
	private final Label label;
	private final IconButton closeTabButton;
	private boolean isSelected;
	
	public TabSheetTabComponent(String caption)
	{
		super();
		
		this.label = new Label(caption);
		this.label.setStyleName("custom-tabsheet-tabs-tab-label");
		this.closeTabButton = new IconButton(ThemeResources.img_closeIcon16);
		
		HorizontalLayout innerLayout = new HorizontalLayout();
		innerLayout.addComponent(label);
		innerLayout.addComponent(closeTabButton);
		innerLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		innerLayout.setComponentAlignment(closeTabButton, Alignment.MIDDLE_RIGHT);
		innerLayout.setSpacing(true);
		
		this.isSelected = false;
		setContent(innerLayout);
	}
	
	@Override
	public String getCaption()
	{
		return label.getValue();
	}
	
	@Override
	public void setCaption(String caption)
	{
		label.setValue(caption);
	}
	
	public boolean canCloseTab()
	{
		return true; // just some default implementation - override in subclasses
	}
	
	public void addCloseHandler(ClickListener closeHandler)
	{
		this.closeTabButton.addClickListener(closeHandler);
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean selected)
	{
		if(isSelected() != selected)
		{
			invertSelection();
		}
	}
	
	private void invertSelection()
	{
		// invert visual looks
		if(isSelected())
		{
			removeStyleName("custom-tabsheet-tabs-tab-selected");
		}
		else
		{
			addStyleName("custom-tabsheet-tabs-tab-selected");
		}
		
		// invert state
		isSelected = !isSelected;
	}
}