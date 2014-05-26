package org.pikater.web.vaadin.gui.server.webui.experimenteditor;

import org.pikater.web.vaadin.gui.server.components.kineticcomponent.KineticComponent;
import org.pikater.web.vaadin.gui.server.components.tabsheet.TabSheetTabComponent;

public class CustomTabSheetTabComponent extends TabSheetTabComponent
{
	private static final long serialVersionUID = 2019691593787134700L;
	
	private final KineticComponent contentComponent;
	
	public CustomTabSheetTabComponent(String caption, KineticComponent contentComponent)
	{
		super(caption);
		
		this.contentComponent = contentComponent;
		this.contentComponent.setParentTab(this);
	}
	
	@Override
	public boolean canCloseTab()
	{
		return !contentComponent.isContentModified();
	}
	
	public KineticComponent getContentComponent()
	{
		return contentComponent;
	}
	
	public void setTabContentModified(boolean modified)
	{
		if(modified)
		{
			if(!getTabCaption().getValue().startsWith("* "))
			{
				getTabCaption().setValue("* " + getTabCaption().getValue());
			}
		}
		else
		{
			if(getTabCaption().getValue().startsWith("* "))
			{
				getTabCaption().setValue(getTabCaption().getValue().substring(2));
			}
		}
	}
}
