package org.pikater.web.vaadin.gui.shared.kineticcomponent.visualstyle;

import net.edzard.kinetic.Colour;

import org.pikater.web.vaadin.gui.client.kineticengine.graph.IGraphItemSettings;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.AbstractGraphItemClient.VisualStyle;

public class KineticBoxSettings implements IGraphItemSettings
{
	private final boolean iconsVisible;
	private final double scale;
	
	public KineticBoxSettings(boolean iconsVisible, double scale)
	{
		this.iconsVisible = iconsVisible;
		this.scale = scale;
	}
	
	public boolean isIconsVisible()
	{
		return iconsVisible;
	}

	public double getScale()
	{
		return scale;
	}

	/*
	private static final double minWidth = 125;
	private static final double maxWidth = 225;
	private static final double difference = maxWidth - minWidth;
	
	private Vector2d currentSize = getSizeFromWidth(175); // default
	
	public void setBoxSize(int percent)
	{
		switch (percent)
		{
			case -1: // scale to browser width
				currentSize = getSizeFromWidth(normalize(Window.getClientWidth() / 800));
				break;
			default: // scale to the percent factor provided 
				currentSize = getSizeFromWidth(minWidth + difference * percent / 100); 
				break;
		}
	}
	
	private static double normalize(double width)
	{
		if(width < minWidth)
		{
			return minWidth;
		}
		else if(width > maxWidth)
		{
			return maxWidth;
		}
		else
		{
			return width;
		}
	}
	
	private static Vector2d getSizeFromWidth(double width)
	{
		return new Vector2d(width, width / 2); // width is double height
	}
	*/
	
	//--------------------------------------------------------------
	// SIZING
	
	public int getBoxWidth()
	{
		return 175;
	}
	
	public int getBoxHeight()
	{
		return getBoxWidth() / 2;
	}
	
	public int getIconWidth()
	{
		return 48;
	}
	
	public int getIconHeight()
	{
		return 48;
	}
	
	//--------------------------------------------------------------
	// SOME STATIC METHODS
	
	public static Colour getColor(VisualStyle graphItemStyle)
	{
		switch(graphItemStyle)
		{
			case SELECTED:
				return Colour.gold;
			case HIGHLIGHTED_EDGE:
				return Colour.red;
			case HIGHLIGHTED_SLOT:
				return Colour.lime;
			
			default:
				return Colour.black;
		}
	}
}