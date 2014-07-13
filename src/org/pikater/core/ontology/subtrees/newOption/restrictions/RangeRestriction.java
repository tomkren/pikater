package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class RangeRestriction implements IRestriction
{
	private static final long serialVersionUID = -9196397486608350955L;

	private ITypedValue minValue;
	private ITypedValue maxValue;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public RangeRestriction()
	{
	}
	public RangeRestriction(ITypedValue minValue, ITypedValue maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public ITypedValue getMinValue()
	{
		return minValue;
	}
	public void setMinValue(ITypedValue minValue)
	{
		this.minValue = minValue;
	}
	public ITypedValue getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(ITypedValue maxValue)
	{
		this.maxValue = maxValue;
	}
	
	public boolean contains(ITypedValue value)
	{
		if(isValid())
		{
			if (minValue.getClass().equals(value.getClass()))
			{
				if (value instanceof DoubleValue || value instanceof FloatValue || value instanceof IntegerValue)
				{
					Number min = (Number) minValue.getValue();
					Number max = (Number) maxValue.getValue();
					Number val = (Number) value.getValue();
					
					return min.doubleValue() <= val.doubleValue() && val.doubleValue() <= max.doubleValue();
				}
				else
				{
					throw new IllegalStateException(String.format("Unimplemented for type: '%s'", value.getClass().getName()));
				}
			}
			else
			{
				throw new IllegalArgumentException("Given value's type does not match the restriction.");
			}
		}
		else
		{
			throw new IllegalStateException("This restriction is not valid.");
		}
	}

	@Override
	public ValueType getType()
	{
		return new ValueType(minValue.getClass());
	}

	@Override
	public boolean isValid()
	{
		if((minValue != null) && (maxValue != null))
		{
			return minValue.getClass().equals(maxValue.getClass());
		}
		else
		{
			return false;
		}
	}

	@Override
	public RangeRestriction clone()
	{
		return new RangeRestriction(minValue.clone(), maxValue.clone());
	}
}
