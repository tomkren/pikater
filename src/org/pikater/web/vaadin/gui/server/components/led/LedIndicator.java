package org.pikater.web.vaadin.gui.server.components.led;

import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Panel;

@StyleSheet("ledIndicator.css")
public class LedIndicator extends Panel
{
	private static final long serialVersionUID = 1770927441412413922L;
	
	private final Anchor actualLed;
	
	public LedIndicator(LedIndicatorTheme theme)
	{
		this(theme, null);
	}

	public LedIndicator(LedIndicatorTheme theme, ClickListener listener)
	{
		super();
		this.actualLed = new Anchor(null, listener);
		this.actualLed.setWidth("10px");
		this.actualLed.setHeight("10px");
		this.actualLed.removeStyleName("v-label");
		
		setSizeUndefined();
		setContent(actualLed);
		
		setTheme(theme);
		setStyleName("ledIndicator");
		setHoverEnabled(listener != null);
	}
	
	public void setHoverEnabled(boolean enabled)
	{
		if(enabled)
		{
			actualLed.addStyleName("hoverEnabled");
		}
		else
		{
			actualLed.removeStyleName("hoverEnabled");
		}
	}
	
	public void setTheme(LedIndicatorTheme theme)
	{
		actualLed.setStyleName(theme.toStyleName());
	}
}