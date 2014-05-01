package org.pikater.web.experiment.options;

import java.util.Collection;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EnumeratedValueParameter")
public class ArrayOption<T> extends AbstractOption<T>
{
    public ArrayOption(Collection<T> values)
	{
		super();
		
		this.values.addAll(values);
	}
    
    public List<T> getValuesCollection()
    {
    	return values;
    }

	@Override
	public UniversalOption export()
	{
		String[] result = new String[values.size()];
		int index = 0;
		for(T value : values)
		{
			result[index] = exportValue(value);
			index++;
		}
		return new UniversalOption(result);
	}
}
