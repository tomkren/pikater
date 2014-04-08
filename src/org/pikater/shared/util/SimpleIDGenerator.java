package org.pikater.shared.util;

public class SimpleIDGenerator
{
	private Integer currentID;

	public SimpleIDGenerator()
	{
		this.currentID = 1;
	}
	
	public Integer getAndIncrement()
	{
		currentID++;
		return currentID - 1;
	}
}
