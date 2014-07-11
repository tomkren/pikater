package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import java.io.Serializable;

public class EdgeGraphItemShared implements Serializable
{
	private static final long serialVersionUID = 4411734178488130587L;
	
	public String fromBoxID;
	public String toBoxID;
	
	/** Keeps GWT and Vaadin happy */
	public EdgeGraphItemShared()
	{
	}

	public EdgeGraphItemShared(String fromBoxID, String toBoxID)
	{
		this.fromBoxID = fromBoxID;
		this.toBoxID = toBoxID;
	}
}