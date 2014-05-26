package org.pikater.web.vaadin.gui.shared;

public enum KineticComponentClickMode
{
	SELECTION,
	CONNECTION;
	
	public KineticComponentClickMode getOther()
	{
		switch (this)
		{
			case CONNECTION:
				return SELECTION;
			case SELECTION:
				return CONNECTION;
			default:
				throw new IllegalStateException("Unknown click mode: " + name());
		}
	}
}
