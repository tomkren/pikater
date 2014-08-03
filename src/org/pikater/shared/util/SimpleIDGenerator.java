package org.pikater.shared.util;

import java.io.Serializable;

public class SimpleIDGenerator implements Serializable
{
	private static final long serialVersionUID = -8774263381709855657L;
	
	private Integer currentID;

	public SimpleIDGenerator()
	{
		this.currentID = getFirstID();
	}
	
	public Integer getAndIncrement()
	{
		currentID++;
		return currentID - 1;
	}
	
	public static Integer getFirstID()
	{
		return 0; // this is required by some of the referencing code
	}
}
