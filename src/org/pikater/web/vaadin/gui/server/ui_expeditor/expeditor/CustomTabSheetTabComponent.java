package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.customtabsheet.TabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.KineticComponent;

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
		if(contentComponent.getExperimentGraph() != null)
		{
			return contentComponent.getExperimentGraph().isEmpty(); // TODO: content modified
		}
		else
		{
			return true;
		}
	}
	
	public KineticComponent getContentComponent()
	{
		return contentComponent;
	}
	
	public void setTabContentModified(boolean modified)
	{
		if(modified)
		{
			if(!getCaption().startsWith("* "))
			{
				setCaption("* " + getCaption());
			}
		}
		else
		{
			if(getCaption().startsWith("* "))
			{
				setCaption(getCaption().substring(2));
			}
		}
	}
}
