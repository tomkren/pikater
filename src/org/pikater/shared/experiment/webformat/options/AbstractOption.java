package org.pikater.shared.experiment.webformat.options;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOption<T>
{
	public final String name;
	public final String description;
	public final String synopsis;
	
	protected final List<T> values;
	
	public AbstractOption(String name, String description, String synopsis)
	{
		this.name = name;
		this.description = description;
		this.synopsis = synopsis;
		this.values = new ArrayList<T>();
	}
	
	protected String exportValue(T value)
	{
		// TODO: untested
		return value.toString();
	}
}