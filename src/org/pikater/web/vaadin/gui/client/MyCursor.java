package org.pikater.web.vaadin.gui.client;

public enum MyCursor
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
