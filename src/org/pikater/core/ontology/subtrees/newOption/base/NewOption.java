package org.pikater.core.ontology.subtrees.newOption.base;

import jade.content.Concept;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

import com.thoughtworks.xstream.XStream;

public class NewOption implements Concept
{
	private static final long serialVersionUID = 972224767505690979L;
	
	private String name;
	private String description;
	private boolean immutable;
	
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
		this.immutable = false;
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

	public boolean isImmutable()
	{
		return immutable;
	}

    public boolean isMutable()
    {
        return !immutable;
    }

	public void setImmutable(boolean immutable)
	{
		this.immutable = immutable;
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
			return valuesWrapper.getValue(0);
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
	
	@Override
	public NewOption clone()
	{
		NewOption result = new NewOption(name, valuesWrapper.clone(), valueRestrictions.clone());
		result.setDescription(getDescription());
		result.setImmutable(this.isImmutable());
		return result;
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
						valuesWrapper.getValue(i).getType()))
				{
					return false;
				}
			}
		}
		return valuesWrapper.isValid() && valueRestrictions.isValid();
	}

	public String exportToWeka()
	{
		return "-" + this.getName() + valuesWrapper.exportToWeka();
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
}