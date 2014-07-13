package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;

public class Value implements Concept
{
	private static final long serialVersionUID = 4778874898115080831L;

	private ITypedValue defaultValue;
	private ITypedValue currentValue;
	private ValueType type;

	/**
	 * Used to create an empty value suitable for multi-value options.
	 */
	@Deprecated
	public Value()
	{
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 */
	public Value(ITypedValue defaultValue)
	{
		this(defaultValue, null, null);
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 */
	public Value(ITypedValue defaultValue, RangeRestriction rangeRestriction)
	{
		this(defaultValue, rangeRestriction, null);
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 */
	public Value(ITypedValue defaultValue, SetRestriction setRestriction)
	{
		this(defaultValue, null, setRestriction);
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 */
	public Value(ITypedValue defaultValue, RangeRestriction rangeRestriction, SetRestriction setRestriction)
	{
		this.defaultValue = defaultValue;
		this.currentValue = this.defaultValue.clone();
		this.type = new ValueType(defaultValue.getClass(), rangeRestriction, setRestriction);
	}
	/**
	 * Copy-constructor for internal use.
	 * @return
	 */
	private Value(ValueType type, ITypedValue defaultValue, ITypedValue currentValue)
	{
		this.defaultValue = defaultValue;
		this.currentValue = currentValue;
		this.type = type;
	}

	public ITypedValue getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(ITypedValue defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public ITypedValue getCurrentValue()
	{
		return currentValue;
	}
	public void setCurrentValue(ITypedValue currentValue)
	{
		this.currentValue = currentValue;
	}
	public ValueType getType()
	{
		return type;
	}
	public void setType(ValueType type)
	{
		this.type = type;
	}
	
	public boolean isValid()
	{
		if(currentValue == null)
		{
			return false;
		}
		else if(!type.getTypeClass().equals(currentValue.getClass()))
		{
			return false;
		}
		else if((defaultValue != null) && !defaultValue.getClass().equals(currentValue.getClass()))
		{
			return false;
		}
		else
		{
			return type.isValid();
		}
	}

	@Override
	public Value clone()
	{
		return new Value(
				type.clone(),
				getDefaultValue() != null ? getDefaultValue().clone() : null,
				currentValue.clone()
		);
	}
}
