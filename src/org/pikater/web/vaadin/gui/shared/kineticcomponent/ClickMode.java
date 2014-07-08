package org.pikater.web.vaadin.gui.shared.kineticcomponent;

public enum ClickMode
{
	SELECTION,
	CONNECTION;
	
	public ClickMode getOther()
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
