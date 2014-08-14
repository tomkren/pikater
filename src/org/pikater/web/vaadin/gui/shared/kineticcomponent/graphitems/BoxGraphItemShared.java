package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import java.io.Serializable;

import org.pikater.shared.experiment.webformat.IBoxInfoCommon;

public class BoxGraphItemShared implements Serializable, IBoxInfoCommon<Integer>
{
	private static final long serialVersionUID = -7700316337241284638L;
	
	public Integer boxID;
	public int positionX;
	public int positionY;
	
	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public BoxGraphItemShared()
	{
		this(0, 0, 0);
	}

	public BoxGraphItemShared(Integer boxID, int positionX, int positionY)
	{
		this.boxID = boxID;
		this.positionX = positionX;
		this.positionY = positionY;
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

	@Override
	public int getPosX()
	{
		return positionX;
	}

	@Override
	public void setPosX(int posX)
	{
		this.positionX = posX;
	}

	@Override
	public int getPosY()
	{
		return positionY;
	}

	@Override
	public void setPosY(int posY)
	{
		this.positionY = posY;
	}
}