package org.pikater.shared.experiment.parameters;

import org.pikater.shared.util.Interval;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RangedValueParameter")
public class RangedValueParameter<T extends Comparable<T>> extends ValueParameter<T>
{
    public final Interval<T> range;
    public final boolean isStrict;
    
    public RangedValueParameter(T value, Interval<T> range, boolean isStrict)
	{
		super(value);
		
		this.range = range;
		this.isStrict = isStrict;
	}
    
    @Override
    public void setValue(T value)
    {
    	if(isStrict)
    	{
    		if((range.min.compareTo(value) <= 0) && (value.compareTo(range.max) <= 0)) // value is in the specified range (bounds included)
    		{
    			super.setValue(value);
    		}
    		else
    		{
    			throw new IllegalArgumentException();
    		}
    	}
    	else
    	{
    		super.setValue(value);
    	}
    }
}
