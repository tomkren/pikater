package org.pikater.web.experiment_backup.options;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ValueParameter")
public class ValueOption<T extends Comparable <T>> extends AbstractOption<T>
{
	public ValueOption(T value)
	{
		super();
		
		setValue(value);
	}
	
	public void setValue(T value)
	{
		this.values.clear();
		this.values.add(value);
    }
    
	public T getValue()
	{
		if(values.isEmpty())
		{
			return null;
		}
		else
		{
			return values.get(0);
		}
    }

	@Override
	public UniversalOption export()
	{
		return new UniversalOption(exportValue(getValue()));
	}
}
