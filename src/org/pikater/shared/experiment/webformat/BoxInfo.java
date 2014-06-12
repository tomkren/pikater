package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class BoxInfo implements Serializable
{
	private static final long serialVersionUID = 3875674558654733345L;
	
	public String boxID;
	public String boxTypeName;
	public String displayName;
	public int initialX;
	public int initialY;
	
	/** Keeps GWT and Vaadin happy */
	protected BoxInfo()
	{
	}
	
	public BoxInfo(String boxID, String boxTypeName, String displayName, int initialX, int initialY)
	{
		this.boxID = boxID;
		this.boxTypeName = boxTypeName;
		this.displayName = displayName;
		this.initialX = initialX;
		this.initialY = initialY;
	}
}