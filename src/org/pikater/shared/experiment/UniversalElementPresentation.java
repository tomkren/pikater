package org.pikater.shared.experiment;

/**
 * <p>Additional web related information for the parent {@link UniversalElement
 * element} representing a box in the web experiment editor.</p>
 * 
 * <p>Core system doesn't use this class at all.</p>
 * 
 * @author stepan
 */
public class UniversalElementPresentation
{
	/**
	 * X position of the box represented by the parent element.
	 */
	private int x;
	
	/**
	 * Y position of the box represented by the parent element.
	 */
	private int y;
	
	/**
	 * Gets the X position of the box represented by the parent element.
	 */
	public int getX()
	{
		return x;
	}
	/**
	 * Sets the X position of the box represented by the parent element.
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * Gets the Y position of the box represented by the parent element.
	 */
	public int getY()
	{
		return y;
	}
	/**
	 * Sets the Y position of the box represented by the parent element.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
}