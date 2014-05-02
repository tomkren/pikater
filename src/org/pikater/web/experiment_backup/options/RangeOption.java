package org.pikater.web.experiment_backup.options;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RangedValueParameter")
public class RangeOption<T extends Comparable<T>> extends AbstractOption<T>
{
    public RangeOption(T min, T max)
	{
    	super();
    	
    	assert(min.compareTo(max) < 0);
		values.add(min);
		values.add(max);
	}
    
    public T getMin()
    {
    	return values.get(0);
    }
    
    public T getMax()
    {
    	return values.get(1);
    }
    
    public void setMin(T value)
    {
    	assert(value.compareTo(getMax()) < 0);
    	values.set(0, value);
    }
    
    public void setMax(T value)
    {
    	assert(getMin().compareTo(value) < 0);
    	values.set(1, value);
    }

	@Override
	public UniversalOption export()
	{
		return new UniversalOption(exportValue(getMin()), exportValue(getMax()));
	}
}
