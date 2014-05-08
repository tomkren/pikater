package org.pikater.shared.experiment.webformat.options;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ValueParameter")
public class ValueOption<T> extends AbstractOption<T>
{
	private static final long serialVersionUID = -9000045022848736919L;

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
