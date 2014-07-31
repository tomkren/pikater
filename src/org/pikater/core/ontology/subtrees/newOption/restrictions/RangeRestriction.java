package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class RangeRestriction implements IRestriction
{
	private static final long serialVersionUID = -9196397486608350955L;

	private IValueData minValue;
	private IValueData maxValue;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public RangeRestriction()
	{
	}
	public RangeRestriction(IValueData minValue, IValueData maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public IValueData getMinValue()
	{
		return minValue;
	}
	public void setMinValue(IValueData minValue)
	{
		this.minValue = minValue;
	}
	public IValueData getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(IValueData maxValue)
	{
		this.maxValue = maxValue;
	}
	
	@Override
	public boolean isValid()
	{
		if((minValue == null) || (maxValue == null) || !minValue.getClass().equals(maxValue.getClass()))
		{
			return false;
		}
		else if (!(minValue instanceof IComparableValueData) || 
				(((IComparableValueData) minValue).compareTo((IComparableValueData) maxValue) > 0))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	@Override
	public boolean isValidAgainst(Object obj)
	{
		if(isValid() && obj.getClass().equals(minValue.getClass()))
		{
			IComparableValueData minComp = (IComparableValueData) minValue; 
			IComparableValueData maxComp = (IComparableValueData) maxValue;
			IComparableValueData valueComp = (IComparableValueData) obj;
			return (minComp.compareTo(valueComp) <= 0) && (valueComp.compareTo(maxComp) <= 0); 
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
