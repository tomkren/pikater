package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class ValueType implements Concept, IValidated
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
	 * Set default value via a constructor - this method should only be used by JADE.
	 * @param defaultValue
	 */
	@Deprecated
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
	
	/**
	 * The following conditions are checked (all of them are assumed in web package, so
	 * don't change them lightly):
	 * - Default value is set.
	 * - At least one of range and set restrictions is not set.
	 * - No restriction is set for question mark values.
	 * - If a restriction is set, it needs to be valid.
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
		else if (((defaultValue instanceof QuestionMarkRange) || (defaultValue instanceof QuestionMarkSet)) &&
				(isRangeRestrictionDefined() || isSetRestrictionDefined()))
		{
			return false;
		}
		else if ((defaultValue instanceof StringValue) && isRangeRestrictionDefined())
		{
			return false;
		}
		else if(isRangeRestrictionDefined() && !rangeRestriction.isValid())
		{
			return false;
		}
		else if(isSetRestrictionDefined() && !setRestriction.isValid())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	@Override
	public ValueType clone()
	{
		return new ValueType(
				defaultValue.clone(),
				rangeRestriction != null ? rangeRestriction.clone() : null,
				setRestriction != null ? setRestriction.clone() : null
		);
	}
}