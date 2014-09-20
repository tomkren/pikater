package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class ValueType implements Concept, IValidated, ICloneable
{
	private static final long serialVersionUID = -4658896847448815807L;

	/**
	 * Required to be set (also by {@link Value} class). Should not be arbitrarily changed.
	 */
	private IValueData defaultValue;
	
	/*
	 * Only one of these should be set.
	 */
	private RangeRestriction rangeRestriction;
	private SetRestriction setRestriction;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public ValueType()
	{
	}
	/** 
	 * @param defaultValue Seriously, don't try to pass in a null...
	 */
	public ValueType(IValueData defaultValue)
	{
		this(defaultValue, null, null);
	}
	/** 
	 * @param defaultValue Seriously, don't try to pass in a null...
	 * @param rangeRestriction
	 */
	public ValueType(IValueData defaultValue, RangeRestriction rangeRestriction)
	{
		this(defaultValue, rangeRestriction, null);
	}
	/** 
	 * @param defaultValue Seriously, don't try to pass in a null...
	 * @param setRestriction
	 */
	public ValueType(IValueData defaultValue, SetRestriction setRestriction)
	{
		this(defaultValue, null, setRestriction);
	}
	/**
	 * Full constructor for internal use.
	 * @param defaultValue
	 * @param rangeRestriction
	 * @param setRestriction
	 */
	private ValueType(IValueData defaultValue, RangeRestriction rangeRestriction, SetRestriction setRestriction)
	{
		this.defaultValue = defaultValue;
		this.rangeRestriction = rangeRestriction;
		this.setRestriction = setRestriction;
	}
	
	public IValueData getDefaultValue()
	{
		return defaultValue;
	}
	/**
	 * Use only if you really know what you're doing.
	 * @param defaultValue
	 */
	public void setDefaultValue(IValueData defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public RangeRestriction getRangeRestriction()
	{
		return rangeRestriction;
	}
	public void setRangeRestriction(RangeRestriction rangeRestriction)
	{
		this.rangeRestriction = rangeRestriction;
	}
	public SetRestriction getSetRestriction()
	{
		return setRestriction;
	}
	public void setSetRestriction(SetRestriction setRestriction)
	{
		this.setRestriction = setRestriction;
	}
	
	/*
	 * Some convenience interface.
	 */
	public boolean isRangeRestrictionDefined()
	{
		return getRangeRestriction() != null;
	}
	public boolean isSetRestrictionDefined()
	{
		return getSetRestriction() != null;
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
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime
				* result
				+ ((rangeRestriction == null) ? 0 : rangeRestriction.hashCode());
		result = prime * result
				+ ((setRestriction == null) ? 0 : setRestriction.hashCode());
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
		ValueType other = (ValueType) obj;
		if (defaultValue == null)
		{
			if (other.defaultValue != null)
				return false;
		}
		else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (rangeRestriction == null)
		{
			if (other.rangeRestriction != null)
				return false;
		}
		else if (!rangeRestriction.equals(other.rangeRestriction))
			return false;
		if (setRestriction == null)
		{
			if (other.setRestriction != null)
				return false;
		}
		else if (!setRestriction.equals(other.setRestriction))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	/**
	 * Conditions checked in this method are assumed in web package, so
	 * don't change them lightly:
	 * @return 
	 */
	@Override
	public boolean isValid()
	{
		if(defaultValue == null)
		{
			return false;
		}
		else if(isRangeRestrictionDefined() && isSetRestrictionDefined())
		{
			return false;
		}
		else if(isRangeRestrictionDefined())
		{
			if(!rangeRestriction.isValid())
			{
				return false;
			}
			else if(defaultValue instanceof IComparableValueData) 
			{
				return rangeRestriction.validatesValue((IComparableValueData) defaultValue);
			}
			else if(defaultValue instanceof QuestionMarkRange)
			{
				QuestionMarkRange qmr = (QuestionMarkRange) defaultValue;
				if(!qmr.isValid())
				{
					return false;
				}
				else
				{
					return rangeRestriction.isMasterRangeOf(qmr.getUserDefinedRestriction());
				}
			}
			else
			{
				return false;
			}
		}
		else if(isSetRestrictionDefined())
		{
			if(!setRestriction.isValid())
			{
				return false;
			}
			else if((defaultValue instanceof BooleanValue) || (defaultValue instanceof NullValue) || (defaultValue instanceof QuestionMarkRange))
			{
				return false;
			}
			else if(defaultValue instanceof QuestionMarkSet)
			{
				QuestionMarkSet qms = (QuestionMarkSet) defaultValue;
				if(!qms.isValid())
				{
					return false;
				}
				else
				{
					return setRestriction.isMasterSetOf(qms.getUserDefinedRestriction());
				}
			}
			else
			{
				return setRestriction.validatesValue(defaultValue);
			}
		}
		else
		{
			return true;
		}
	}
	@Override
	public ValueType clone()
	{
		ValueType result;
		try
		{
			result = (ValueType) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setDefaultValue(defaultValue.clone());
		result.setRangeRestriction(rangeRestriction != null ? rangeRestriction.clone() : null);
		result.setSetRestriction(setRestriction != null ? setRestriction.clone() : null);
		return result;
	}
}