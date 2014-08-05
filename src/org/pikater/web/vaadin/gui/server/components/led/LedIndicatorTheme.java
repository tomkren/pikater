package org.pikater.web.vaadin.gui.server.components.led;

public enum LedIndicatorTheme
{
	GREEN,
	RED;
	
	public String toStyleName()
	{
		switch(this)
		{
			case GREEN:
				return "themeGreen";
			case RED:
				return "themeRed";
			default:
				throw new IllegalStateException("Unknown state: " + name());
		}
	}
}