package org.pikater.web.experiment_backup.options;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOption<T>
{
	protected final List<T> values;
	
	public AbstractOption()
	{
		this.values = new ArrayList<T>();
	}
	
	protected String exportValue(T value)
	{
		// TODO: untested
		return value.toString();
	}
	
	public abstract UniversalOption export();
}