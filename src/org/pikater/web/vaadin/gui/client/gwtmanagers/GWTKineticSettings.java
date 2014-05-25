package org.pikater.web.vaadin.gui.client.gwtmanagers;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;

import com.google.gwt.user.client.Window;

import net.edzard.kinetic.Vector2d;

public class GWTKineticSettings
{
	//----------------------------------------------------------------------------
	// BOX DEFINITIONS
	
	private static BoxInfoCollection boxDefinitions = null;
	
	public static BoxInfoCollection getBoxDefinitions()
	{
		if(boxDefinitions == null)
		{
			throw new NullPointerException("Box definitions have not yet been globally set: they are null.");
		}
		else
		{
			return boxDefinitions;
		}
	}
	
	public static void setBoxDefinitions(BoxInfoCollection boxDefinitions)
	{
		GWTKineticSettings.boxDefinitions = boxDefinitions;
	}
	
	//----------------------------------------------------------------------------
	// PUBLIC GETTERS
	
	private static final double minWidth = 100;
	private static final double maxWidth = 200;
	private static final double difference = maxWidth - minWidth;
	private static Vector2d currentSize = getSizeFromWidth(maxWidth); // default
	
	public static Vector2d getCurrentBoxSize()
	{
		return currentSize; 
	}
	
	public static void setBoxSize(int percent)
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
}
