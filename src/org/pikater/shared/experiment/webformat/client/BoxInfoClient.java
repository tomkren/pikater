package org.pikater.shared.experiment.webformat.client;

import java.io.Serializable;

import org.pikater.shared.experiment.webformat.IBoxInfoCommon;

public class BoxInfoClient implements Serializable, IBoxInfoCommon<Integer>
{
	private static final long serialVersionUID = 3875674558654733345L;
	
	/*
	 * General info.
	 */
	public Integer boxID;
	public String boxTypeName;
	public String displayName;
	
	/*
	 * Position.
	 */
	public int positionX;
	public int positionY;
	
	/*
	 * Picture info. 
	 */
	public String pictureURL;

	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	@Deprecated
	public BoxInfoClient()
	{
	}

	/** 
	 * @param boxID
	 * @param boxTypeName
	 * @param displayName
	 * @param posX
	 * @param posY
	 * @param pictureURL
     */
	public BoxInfoClient(Integer boxID, String boxTypeName, String displayName, int posX, int posY, String pictureURL)
	{
		this.boxID = boxID;
		this.boxTypeName = boxTypeName;
		this.displayName = displayName;
		this.positionX = posX;
		this.positionY = posY;
		this.pictureURL = pictureURL;
	}

	@Override
	public Integer getID()
	{
		return boxID;
	}

	@Override
	public void setID(Integer id)
	{
		this.boxID = id;
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