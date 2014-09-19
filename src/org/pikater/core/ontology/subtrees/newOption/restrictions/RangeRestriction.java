package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;

public class RangeRestriction implements IRestriction
{
	private static final long serialVersionUID = -9196397486608350955L;

	private IComparableValueData minValue;
	private IComparableValueData maxValue;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public RangeRestriction()
	{
	}
	public RangeRestriction(IComparableValueData minValue, IComparableValueData maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public IComparableValueData getMinValue()
	{
		return minValue;
	}
	public void setMinValue(IComparableValueData minValue)
	{
		this.minValue = minValue;
	}
	public IComparableValueData getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(IComparableValueData maxValue)
	{
		this.maxValue = maxValue;
	}
	
	/*
	 * Some convenience interface.
	 */
	public Class<? extends IComparableValueData> fetchRangeType()
	{
		if(minValue != null)
		{
			return minValue.getClass();
		}
		else if(maxValue != null)
		{
			return maxValue.getClass();
		}
		else
		{
			return null;
		}
	}
	public boolean isFullySpecified()
	{
		return (minValue != null) && (maxValue != null); 
	}
	public boolean isMasterRangeOf(RangeRestriction other)
	{
		if(isValid() && other.isValid() && fetchRangeType().equals(other.fetchRangeType()))
		{
			if(isFullySpecified() != other.isFullySpecified())
			{
				return false;
			}
			else
			{
				// ranges with only opposite bounds defined can not be compatible
				if(!isFullySpecified())
				{
					if((minValue != null) && (other.getMaxValue() != null))
					{
						return false;
					}
					if((maxValue != null) && (other.getMinValue() != null))
					{
						return false;
					}
				}
				
				// check the "subrange" relation
				if((minValue != null) && (other.getMinValue() != null) && (minValue.compareTo(other.getMinValue()) > 0))
				{
					return false;
				}
				if((maxValue != null) && (other.getMaxValue() != null) && (other.getMaxValue().compareTo(maxValue) > 0))
				{
					return false;
				}
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	public boolean validatesValue(IComparableValueData value)
	{
		if(isValid())
		{
			if((minValue != null) && minValue.getClass().equals(value.getClass()) && (minValue.compareTo(value) > 0))
			{
				return false;
			}
			if((maxValue != null) && maxValue.getClass().equals(value.getClass()) && (value.compareTo(maxValue) > 0))
			{
				return false;
			}
			return true;
		}
		else
		{
			return false;
		}
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
		if((minValue == null) && (maxValue == null))
		{
			return false;
		}
		else if(isFullySpecified() && (!minValue.getClass().equals(maxValue.getClass()) || (minValue.compareTo(maxValue) > 0))) 
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	@Override
	public RangeRestriction clone() throws CloneNotSupportedException
	{
		RangeRestriction result = (RangeRestriction) super.clone();
		result.setMinValue(minValue == null ? null : (IComparableValueData) minValue.clone());
		result.setMaxValue(maxValue == null ? null : (IComparableValueData) maxValue.clone());
		return result;
	}
}
