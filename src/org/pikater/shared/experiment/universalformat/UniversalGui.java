package org.pikater.shared.experiment.universalformat;

import java.io.Serializable;

import net.edzard.kinetic.Vector2d;

public class UniversalGui implements Serializable
{
	private static final long serialVersionUID = 602561730450272920L;
	
	public int x;
	public int y;
	
	/**
	 * Keeps GWT and Vaadin happy.
	 */
	protected UniversalGui()
	{
	}
	
	public UniversalGui(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public UniversalGui(Vector2d absoluteBoxPosition)
	{
		this.x = (int) absoluteBoxPosition.x;
		this.y = (int) absoluteBoxPosition.y;
	}
}
