package org.pikater.core.ontology.subtrees.newOption.base;

import com.thoughtworks.xstream.XStream;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.*;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import java.util.Arrays;
import java.util.List;

public class NewOption implements Concept, IMergeable, IWekaItem
{
	private static final long serialVersionUID = 972224767505690979L;
	
	private String name;
	private String description;
	
	private ValuesForOption valuesWrapper;
	private RestrictionsForOption valueRestrictions;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public NewOption() {}
	
	/**
	 * Creates an option with a single predefined value. Value type and restrictions are inferred.
	 * @param name
	 * @param defaultValue
	 */
	public NewOption(String name, IValueData defaultValue)
	{
		this(name, new Value(defaultValue), new TypeRestriction(Arrays.asList(new ValueType(defaultValue)))); 
	}

    public NewOption(String name, int value)
    {
        this(name,new IntegerValue(value));
    }

    public NewOption(String name, String value)
    {
        this(name,new StringValue(value));
    }

    public NewOption(String name, double value)
    {
        this(name,new DoubleValue(value));
    }

    public NewOption(String name, float value)
    {
        this(name,new FloatValue(value));
    }

	/**
	 * Creates an option with a single predefined value. Value 
	 * type and restrictions are inferred from the arguments.
	 * @param name
	 * @param defaultValue
	 * @param rangeRestriction
	 */
	public NewOption(String name, IValueData defaultValue, RangeRestriction rangeRestriction)
	{
		this(name, new Value(defaultValue, rangeRestriction));
	}
	/**
	 * Creates an option with a single predefined value. Value 
	 * type and restrictions are inferred from the arguments.
	 * @param name
	 * @param defaultValue
	 * @param setRestriction
	 */
	public NewOption(String name, IValueData defaultValue, SetRestriction setRestriction)
	{
		this(name, new Value(defaultValue, setRestriction));
	}
	/**
	 * Creates an option with a single predefined value. Value 
	 * type and restrictions are inferred from the arguments.
	 * @param name
	 * @param value
     */
	public NewOption(String name, Value value)
	{
		this(name, value, new TypeRestriction(Arrays.asList(value.getType())));
	}
	/**
	 * Creates an option with a single predefined value. Value type is inferred, restriction is given.
	 * @param name
	 * @param value <font color="red">make sure the value's type is the same instance as in the 'restriction' param</font>
	 * @param restriction
	 */
	public NewOption(String name, Value value, TypeRestriction restriction)
	{
		this(name, Arrays.asList(value), Arrays.asList(restriction));
	}
	/**
	 * Creates an option with multiple values.
	 * @param name
	 * @param restrictions
	 */
	public NewOption(String name, List<Value> values, List<TypeRestriction> restrictions)
	{
		this(name, new ValuesForOption(values), new RestrictionsForOption(restrictions));
	}
	/**
	 * More or less a copy-constructor; for internal use. 
	 * @param name
	 * @param values
	 * @param restrictions
	 */
	private NewOption(String name, ValuesForOption values, RestrictionsForOption restrictions)
	{
		this.name = name;
		this.valuesWrapper = values;
		this.valueRestrictions = restrictions;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public ValuesForOption getValuesWrapper()
	{
		return valuesWrapper;
	}
	public void setValuesWrapper(ValuesForOption valuesWrapper)
	{
		this.valuesWrapper = valuesWrapper;
	}
	public RestrictionsForOption getValueRestrictions()
	{
		return valueRestrictions;
	}
	public void setValueRestrictions(RestrictionsForOption valueRestrictions)
	{
		this.valueRestrictions = valueRestrictions;
	}

	public boolean isMutable()
    {
        return !isImmutable();
    }
	public boolean isImmutable()
	{
        for (Value value:getValuesWrapper().getValues()) {
            IValueData typedValue = value.getCurrentValue();
            if (typedValue instanceof QuestionMarkRange)
            {
               return false;
            }
            else if (typedValue instanceof QuestionMarkSet)
            {
                return false;
            }
        }
        return true;
	}
	public boolean isEmpty()
	{
		return valuesWrapper.size() == 0;
	}
	public boolean isSingleValue()
	{
		return valuesWrapper.size() == 1;
	}
	public Value toSingleValue()
	{
		if(isSingleValue())
		{
			return valuesWrapper.returnByIndex(0);
		}
		else
		{
			throw new IllegalStateException("This option can not be converted to a single value.");
		}
	}
	public TypeRestriction getValueRestrictionForIndex(int index)
	{
		return valueRestrictions.getByIndex(index);
	}
	public String computeDataType()
	{
		String firstValue = null;
		for (Value valueI : valuesWrapper.getValues())
		{
			if (firstValue == null)
			{
				firstValue = valueI.getCurrentValue().getClass().getSimpleName();
			}
			else
			{
				String currentValue = valueI.getCurrentValue().getClass().getSimpleName(); 
				if (! firstValue.equals(currentValue))
				{
					return "MIXED";
				}
			}
		}
		return firstValue;
	}
	public void resetToDefaultValue()
	{
		//TODO:
	}
	public boolean isValid(boolean validateTypeBinding)
	{
		if(isEmpty())
		{
			return false;
		}
		else if(valuesWrapper.size() != valueRestrictions.size())
		{
			return false;
		}
		else if(validateTypeBinding)
		{
			for(int i = 0; i < valuesWrapper.size(); i++)
			{
				if(!valueRestrictions.getByIndex(i).getTypes().contains(
						valuesWrapper.returnByIndex(i).getType()))
				{
					return false;
				}
			}
		}
		return valuesWrapper.isValid() && valueRestrictions.isValid();
	}
	public String exportXML() {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);
		
		String xml = xstream.toXML(this);

		return xml;
	}
	public static NewOption importXML(String xml) {

		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		NewOption optionNew = (NewOption) xstream
				.fromXML(xml);

		return optionNew;
	}
	
	@Override
	public NewOption clone()
	{
		NewOption result = new NewOption(name, valuesWrapper.clone(), valueRestrictions.clone());
		result.setDescription(getDescription());
		return result;
	}
	@Override
	public void mergeWith(IMergeable other)
	{
		/*
		 * We only need to copy the other option's current value to this
		 * option's current value. Anything else (for example validity
		 * checks) would be counter-productive. This method needs to make
		 * best effort at copying current values despite various incompatibilities
		 * between this option and the other option.
		 * 
		 * IMPORTANT: only merge at the end (when we know we CAN without potentially
		 * breaking anything).
		 */
		
		NewOption otherOption = (NewOption) other;
		if(!isSingleValue() || !otherOption.isSingleValue())
		{
			throw new IllegalArgumentException("One of the options is a multi-value option. Those are not supported.");
		}
		else
		{
			Value currentOptionValue = toSingleValue();
			Value otherOptionValue = otherOption.toSingleValue();
			if(currentOptionValue.getType().getDefaultValue().getClass().equals(
					otherOptionValue.getType().getDefaultValue().getClass()))
			{
				currentOptionValue.setCurrentValue(otherOptionValue.getCurrentValue());
			}
		}
	}
	@Override
	public String exportToWeka()
	{
		return "-" + this.getName() + valuesWrapper.exportToWeka();
	}
}