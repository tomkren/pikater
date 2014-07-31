package org.pikater.web.vaadin.gui.shared.borderlayout;

import java.io.Serializable;

public class Dimension implements Serializable
{
	private static final long serialVersionUID = 6907449348645122997L;
	
	public static enum DimensionMode
	{
		MAX,
		AUTO;
		
		@Override
		public String toString()
		{
			switch(this)
			{
				case AUTO:
					return "auto";
				case MAX:
					return "100%";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	public static enum DimensionUnit
	{
		PCT,
		PX;
		
		public String toString()
		{
			switch(this)
			{
				case PCT:
					return "%";
				case PX:
					return "px";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	public String dimension;
	
	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	@Deprecated
	public Dimension()
	{
	}
	
	public Dimension(DimensionMode dimension)
	{
		this(dimension.toString());
	}
	
	public Dimension(double width, DimensionUnit unit)
	{
		this(String.valueOf(width) + unit.toString());
	}
	
	public Dimension(String dimension)
	{
		this.dimension = dimension;
	}
	
	public boolean isSetToMax()
	{
		return dimension.equals("100%");
	}
}