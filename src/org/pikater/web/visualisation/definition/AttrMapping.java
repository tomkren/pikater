package org.pikater.web.visualisation.definition;

public class AttrMapping implements Comparable<AttrMapping>
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

	@Override
	public int compareTo(AttrMapping o)
	{
		return attrX.compareTo(o.getAttrX());
	}
	
	/**
	 * This is going to be displayed in headers of the result matrix view.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Y: ").append(attrY).append("</br>");
		sb.append("X: ").append(attrX).append("</br>");
		sb.append("=>: ").append(attrTarget);
		return sb.toString();
	}
}