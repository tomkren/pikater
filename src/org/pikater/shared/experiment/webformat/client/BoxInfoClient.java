package org.pikater.shared.experiment.webformat.client;

import java.io.Serializable;

import org.pikater.shared.experiment.webformat.IBoxInfo;

public class BoxInfoClient implements Serializable, IBoxInfo<Integer>
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
	public int initialX;
	public int initialY;
	
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
	 * @param initialX
	 * @param initialY
	 * @param pictureURL
     */
	public BoxInfoClient(Integer boxID, String boxTypeName, String displayName, int initialX, int initialY, String pictureURL)
	{
		this.boxID = boxID;
		this.boxTypeName = boxTypeName;
		this.displayName = displayName;
		this.initialX = initialX;
		this.initialY = initialY;
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
}