package org.pikater.shared.experiment.webformat.slots;

import java.io.Serializable;

public class AbstractSlot implements Serializable
{
	private static final long serialVersionUID = 2928996003745242416L;
	
	public final String type;
	public final String description;
	
	public AbstractSlot(String type, String description)
	{
		this.type = type;
		this.description = description;
	}
}
