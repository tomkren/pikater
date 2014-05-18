package org.pikater.web.vaadin.gui.client.gwtmanagers;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Element;

public class GWTCursorManager
{
	public static enum MyCursor
	{
		AUTO,
		CROSSHAIR,
		DEFAULT,
		POINTER,
		MOVE,
		TEXT,
		WAIT,
		HELP;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
	}
	
	// *************************************************************************************
	// PUBLIC METHODS
	
	public static void setCursorType(Element element, MyCursor cursorType)
	{
		previousCursors.put(element.hashCode(), element.getStyle().getCursor());
		element.getStyle().setProperty("cursor", cursorType.toString());
	}
	
	public static void rollBackCursor(Element element)
	{
		element.getStyle().setProperty("cursor", previousCursors.get(element.hashCode()));
	}
	
	// *************************************************************************************
	// PRIVATE FIELDS
		
	/**
	 * Stores cursor styles for elements when desired and to be changed, so that the change can be rolled back later. 
	 */
	private static final Map<Integer, String> previousCursors = new HashMap<Integer, String>();
}
