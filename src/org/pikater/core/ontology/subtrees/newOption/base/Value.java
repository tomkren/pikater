package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValidatedValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class Value implements Concept, IValidated
{
	private static final long serialVersionUID = 4778874898115080831L;

	/**
	 * Defaults to default value. Can be arbitrarily changed.
	 */
	private IValueData currentValue;
	
	/**
	 * Required to be set. Can be arbitrarily changed.
	 */
	private ValueType type;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public Value()
	{
	}
	/**
	 * Value type is inferred from the argument.
	 * @param defaultValue
	 */
	public Value(IValueData defaultValue)
	{
		this.currentValue = defaultValue.clone();
		this.type = new ValueType(defaultValue);
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 * @param rangeRestriction
	 */
	public Value(IValueData defaultValue, RangeRestriction rangeRestriction)
	{
		this.currentValue = defaultValue.clone();
		this.type = new ValueType(defaultValue, rangeRestriction);
	}
	/**
	 * Value type is inferred from the arguments.
	 * @param defaultValue
	 * @param setRestriction
	 */
	public Value(IValueData defaultValue, SetRestriction setRestriction)
	{
		this.currentValue = defaultValue.clone();
		this.type = new ValueType(defaultValue, setRestriction);
	}
	/**
	 * Copy-constructor for internal use. 
	 */
	private Value(ValueType type, IValueData currentValue)
	{
		this.currentValue = currentValue;
		this.type = type;
	}

	public IValueData getCurrentValue()
	{
		return currentValue;
	}
	/**
	 * If null is given, default value is set as current value.
	 * @param currentValue
	 */
	public void setCurrentValue(IValueData currentValue)
	{
		if(currentValue == null)
		{
			this.currentValue = getType().getDefaultValue().clone();
		}
		else
		{
			this.currentValue = currentValue;
		}
	}
	public ValueType getType()
	{
		return type;
	}
	/**
	 * Don't forget to also call {@link #setCurrentValue(IValueData)} to match this new type.
	 * @param type
	 */
	public void setType(ValueType type)
	{
		if(type == null)
		{
			throw new NullPointerException();
		}
		else
		{
			this.type = type;
		}
	}
	
	/**
	 * Several things are assumed in this method (see other methods of this class).
	 * Checked:</br>
	 * - Type binding: default value matches current value, both match the registered type.
	 * - Type validity: registered type is valid.
	 * - Restrictions: if registered type has a restriction, type binding and validity is checked against it.
	 * @return
	 */
	@Override
	public boolean isValid()
	{
		// this implementation safely assumes that the current value is not null
		if((currentValue instanceof IValidatedValueData) && !((IValidatedValueData) currentValue).isValid())
		{
			return false;
		}
		else if(!type.isValid() || !type.getDefaultValue().getClass().equals(currentValue.getClass())) // this allows the next round
		{
			return false;
		}
		else if(type.isRangeRestrictionDefined())
		{
			if(!(currentValue instanceof QuestionMarkRange) && !type.getRangeRestriction().validatesValue((IComparableValueData) currentValue))
			{
				return false;
			}
		}
		else if(type.isSetRestrictionDefined() && !type.getSetRestriction().validatesValue(currentValue))
		{
			return false;
		}
		return true;
	}
	@Override
	public Value clone()
	{
		return new Value(type.clone(), currentValue.clone());
	}
}