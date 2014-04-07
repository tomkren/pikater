package org.pikater.shared.util;

public class IDGenerator
{
	private Integer currentID;

	public IDGenerator()
	{
		this.currentID = 1;
	}
	
	public Integer getAndIncrement()
	{
		currentID++;
		return currentID - 1;
	}
}
