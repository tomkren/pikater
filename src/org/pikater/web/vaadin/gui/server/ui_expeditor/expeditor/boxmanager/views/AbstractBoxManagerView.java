package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views;

import org.pikater.shared.experiment.webformat.server.BoxType;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.IContextForViews;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractBoxManagerView<S extends Object> extends VerticalLayout
{
	private static final long serialVersionUID = 2129962006939216700L;
	
	private final IContextForViews context;
	private final Label boxIdentificationLabel;
	private S currentSource;
	
	public AbstractBoxManagerView(IContextForViews context)
	{
		super();
		
		this.context = context;
		
		this.boxIdentificationLabel = new Label()
		{
			private static final long serialVersionUID = -3028878769001633631L;

			@Override
			public void setValue(String newStringValue)
			{
				super.setValue("Box: " + newStringValue);
			}
		};
		this.boxIdentificationLabel.setSizeUndefined();
		this.boxIdentificationLabel.setStyleName("emphasizedLabel");
		
		this.currentSource = null;
	}
	
	public void setBoxIdentificationLabel(String label)
	{
		boxIdentificationLabel.setValue(label);
	}
	
	public void refreshBoxIdentificationLabel()
	{
		boxIdentificationLabel.setValue(String.format("%s@%s", 
				BoxType.fromAgentInfo(context.getCurrentBoxDataSource().getAssociatedAgent()).name(),
				context.getCurrentBoxDataSource().getAssociatedAgent().getName()
		));
	}
	
	public void setContentFrom(S source)
	{
		if(currentSource != source)
		{
			try
			{
				if(source == null)
				{
					throw new IllegalArgumentException("Null.");
				}
				else
				{
					validateSource(source);
				}
			}
			catch(IllegalArgumentException e)
			{
				MyNotifications.showError("Can not load the resource", e.getMessage());
				return;
			}
			
			currentSource = source;
			refreshBoxIdentificationLabel();
			refreshContent();
		}
	}
	
	//-------------------------------------------------
	// ABSTRACT INTERFACE
	
	protected abstract void validateSource(S source) throws IllegalArgumentException;
	
	protected abstract void refreshContent();
	
	//-------------------------------------------------
	// PROTECTED INTERFACE
	
	protected IContextForViews getContext()
	{
		return context;
	}
	
	protected Label getBoxIdentificatinComponent()
	{
		return boxIdentificationLabel;
	}
	
	protected S getCurrentSource()
	{
		return currentSource;
	}
}