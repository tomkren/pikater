package org.pikater.web.visualisation.definition;

public class AttrMapping
{
	private final String attrX;
	private final String attrY;
	private final String attrTarget;
	
	public AttrMapping(String attrX, String attrY, String attrTarget)
	{
		this.attrX = attrX;
		this.attrY = attrY;
		this.attrTarget = attrTarget;
	}

	public String getAttrX()
	{
		return attrX;
	}

	public String getAttrY()
	{
		return attrY;
	}

	public String getAttrTarget()
	{
		return attrTarget;
	}
}