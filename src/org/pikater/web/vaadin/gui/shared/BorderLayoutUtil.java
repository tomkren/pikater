package org.pikater.web.vaadin.gui.shared;

public class BorderLayoutUtil
{
	public enum Border
	{
		NORTH,
		EAST,
		WEST,
		SOUTH,
		CENTER;
		
		public Row toRow()
		{
			return Row.valueOf(this.name());
		}
		
		public Column toColumn()
		{
			return Column.valueOf(this.name());
		}
	}
	
	public enum Row
	{
		/*
		 * Note: don't alter the order - it is assumed in other classes.
		 */
		
		NORTH,
		CENTER,
		SOUTH
	}
	
	public enum Column
	{
		/*
		 * Note: don't alter the order - it is assumed in other classes.
		 */
		
		WEST,
		CENTER,
		EAST
	}
	
	public enum DimensionMode
	{
		MAX,
		AUTO;
		
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
	
	public enum DimensionUnit
	{
		PCT,
		PX;
		
		public com.google.gwt.dom.client.Style.Unit toGWTUnit()
		{
			switch(this)
			{
				case PCT:
				case PX:
					return com.google.gwt.dom.client.Style.Unit.valueOf(name());
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
}