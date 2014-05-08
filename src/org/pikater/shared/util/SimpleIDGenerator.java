package org.pikater.shared.util;

import java.io.Serializable;

public class SimpleIDGenerator implements Serializable
{
	private static final long serialVersionUID = -8774263381709855657L;
	
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
