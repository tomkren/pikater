package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class BoxInfo implements Serializable
{
	private static final long serialVersionUID = 3875674558654733345L;
	
	/*
	 * General info.
	 */
	public String boxID;
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
	
	/** Keeps GWT and Vaadin happy */
	protected BoxInfo()
	{
	}

	/** 
	 * @param boxID
	 * @param boxTypeName
	 * @param displayName
	 * @param initialX
	 * @param initialY
	 * @param pictureURL
	 * @param pictureWidth
	 * @param pictureHeight
	 */
	public BoxInfo(String boxID, String boxTypeName, String displayName, int initialX, int initialY, String pictureURL)
	{
		this.boxID = boxID;
		this.boxTypeName = boxTypeName;
		this.displayName = displayName;
		this.initialX = initialX;
		this.initialY = initialY;
		this.pictureURL = pictureURL;
	}
}