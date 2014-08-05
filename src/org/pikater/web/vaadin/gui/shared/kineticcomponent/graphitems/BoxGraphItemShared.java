package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import java.io.Serializable;

import org.pikater.shared.experiment.webformat.IBoxInfo;

public class BoxGraphItemShared implements Serializable, IBoxInfo<Integer>
{
	private static final long serialVersionUID = -7700316337241284638L;
	
	public Integer boxID;
	
	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public BoxGraphItemShared()
	{
	}

	public BoxGraphItemShared(Integer boxID)
	{
		this.boxID = boxID;
	}

	@Override
	public Integer getID()
	{
		return boxID;
	}

	@Override
	public void setID(Integer id)
	{
		boxID = id;
	}
}