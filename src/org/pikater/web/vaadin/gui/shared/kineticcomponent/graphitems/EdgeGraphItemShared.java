package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import java.io.Serializable;

public class EdgeGraphItemShared implements Serializable
{
	private static final long serialVersionUID = 4411734178488130587L;
	
	public Integer fromBoxID;
	public Integer toBoxID;
	
	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public EdgeGraphItemShared()
	{
	}

	public EdgeGraphItemShared(Integer fromBoxID, Integer toBoxID)
	{
		this.fromBoxID = fromBoxID;
		this.toBoxID = toBoxID;
	}
}