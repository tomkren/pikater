package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class BoxInfo implements Serializable
{
	private static final long serialVersionUID = 3875674558654733345L;
	
	public String boxID;
	public Integer agentInfoID;
	public BoxType type;
	public String displayName;
	public int initialX;
	public int initialY;
	
	public BoxInfo(String boxID, Integer agentInfoID, BoxType type, String displayName, int initialX, int initialY)
	{
		this.boxID = boxID;
		this.agentInfoID = agentInfoID;
		this.type = type;
		this.displayName = displayName;
		this.initialX = initialX;
		this.initialY = initialY;
	}
	
	public static BoxInfo getWrapper(String boxID, int initialX, int initialY)
	{
		return new BoxInfo(boxID, null, BoxType.WRAPPER, "Wrapper", initialX, initialY);
	}
}