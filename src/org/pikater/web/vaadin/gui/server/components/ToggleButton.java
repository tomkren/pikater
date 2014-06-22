package org.pikater.web.vaadin.gui.server.components;

import com.vaadin.ui.Button;

public class ToggleButton extends Button
{
	private static final long serialVersionUID = 3545576617348270184L;
	
	private boolean pushed;
	
	public ToggleButton(String caption, final ClickListener listener)
	{
		super(caption);
		addClickListener(new ClickListener() // wrap the provided listener with an inner one
		{
			private static final long serialVersionUID = 6508359213671384849L;
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				// first invert the state
				invertPushState();
				
				// callback to the main listener
				listener.buttonClick(event);
			}
		});
		removeStyleName("v-button");
		setStyleName("toggle-button");
		this.pushed = false;
	}
	
	public void setPushed(boolean pushed)
	{
		if(isPushed() != pushed)
		{
			invertPushState();
		}
	}
	
	public boolean isPushed()
	{
		return pushed;
	}
	
	private void invertPushState()
	{
		// invert visual looks
		if(isPushed())
		{
			removeStyleName("toggle-button-pushed");
		}
		else
		{
			addStyleName("toggle-button-pushed");
		}
		
		// invert state
		pushed = !pushed;
	}
}
