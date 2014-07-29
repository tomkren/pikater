package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import java.io.Serializable;

public class BoxGraphItemShared implements Serializable
{
	private static final long serialVersionUID = -7700316337241284638L;
	
	public String boxID;
	
	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public BoxGraphItemShared()
	{
	}

	public BoxGraphItemShared(String boxID)
	{
		this.boxID = boxID;
	}
}