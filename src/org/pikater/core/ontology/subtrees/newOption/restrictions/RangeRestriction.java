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
	
	/* -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((maxValue == null) ? 0 : maxValue.hashCode());
		result = prime * result
				+ ((minValue == null) ? 0 : minValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangeRestriction other = (RangeRestriction) obj;
		if (maxValue == null)
		{
			if (other.maxValue != null)
				return false;
		}
		else if (!maxValue.equals(other.maxValue))
			return false;
		if (minValue == null)
		{
			if (other.minValue != null)
				return false;
		}
		else if (!minValue.equals(other.minValue))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
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
