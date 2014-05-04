package org.pikater.shared.experiment.webformat.options;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ValueParameter")
public class ValueOption<T> extends AbstractOption<T>
{
	public ValueOption(String name, String description, String synopsis, T value)
	{
		super(name, description, synopsis);
		
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
}
