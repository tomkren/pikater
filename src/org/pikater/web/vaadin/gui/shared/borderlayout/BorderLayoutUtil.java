package org.pikater.web.vaadin.gui.shared.borderlayout;

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
}