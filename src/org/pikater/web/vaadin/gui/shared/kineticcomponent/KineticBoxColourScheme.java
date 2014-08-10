package org.pikater.web.vaadin.gui.shared.kineticcomponent;

import org.pikater.web.vaadin.gui.client.kineticengine.graph.AbstractGraphItemClient.VisualStyle;

import net.edzard.kinetic.Colour;

public class KineticBoxColourScheme
{
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