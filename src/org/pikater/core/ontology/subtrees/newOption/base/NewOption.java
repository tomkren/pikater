package org.pikater.core.ontology.subtrees.newOption.base;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.ValuesForOption;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;

import com.thoughtworks.xstream.XStream;

public class NewOption {

	private String name;
	private String description;
	
	private ValuesForOption values;
	private boolean isMutable = false;
	
	private RestrictionsForOption typeRestrictions;
	
	public NewOption(String name) {
		this.values = new ValuesForOption();
		this.name = name;
	}
	public NewOption(ITypedValue value, String name) {
		this(name);
		this.values.addValue(new Value(value));
	}
	public NewOption(ITypedValue defaultValue, ValueType type, String name) {
		this(name);
		this.values.addValue( new Value(null, defaultValue, type) );
	}
	public NewOption(List<ITypedValue> values, String name) {
		this(name);
		for (ITypedValue valueI : values) {
			this.values.addValue( new Value(valueI) );
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public ValuesForOption getValuesWrapper() {
		return values;
	}
	public void setValuesWrapper(ValuesForOption values) {
		this.values = values;
	}
	
	public boolean isEmpty()
	{
		return values.size() == 0;
	}
	public boolean isSingleValue() {
		return values.size() == 1;
	}
	
	public boolean getIsMutable() {
		return isMutable;
	}
	public void setIsMutable(boolean isMutable) {
		this.isMutable = isMutable;
	}
	
	public RestrictionsForOption getTypeRestrictions() {
		return typeRestrictions;
	}
	public void setTypeRestrictions(RestrictionsForOption typeRestrictions) {
		this.typeRestrictions = typeRestrictions;
	}
	
	public Value convertToSingleValue() {
		
		List<Value> valueList = this.values.getValues();
		if (valueList.size() == 1) {
			return valueList.get(0);
		} else {
			throw new IllegalStateException(
					"This option can't be convert to single value");
		}
	}
	
	public boolean containsQuestionMark() {
		
		for (Value valueI : values.getValues()) {
			ITypedValue ivalueI = valueI.getTypedValue();
			if (ivalueI instanceof QuestionMarkRange ||
					ivalueI instanceof QuestionMarkSet) {
				return true;
			}
		}
		return false;
	}
	
	public String computeDataType() {
		
		String firstValue = null;
		for (Value valueI : values.getValues()) {
			if (firstValue == null) {
				firstValue = valueI.getTypedValue().getClass().getSimpleName();
			} else {
				String currentValue = valueI.getTypedValue().getClass().getSimpleName(); 
				if (! firstValue.equals(currentValue)) {
					return "MIXED";
				}
			}
		}
		return firstValue;
	}
	
	public void resetToDefaultValue() {
		//TODO:
	}

	@Override
	public NewOption clone()
	{
		NewOption newOption = new NewOption(getName());
		newOption.setDescription(getDescription());
		newOption.setIsMutable(this.getIsMutable());
		newOption.setValuesWrapper(values.clone());	
		newOption.setTypeRestrictions(typeRestrictions.clone());
		return newOption;
	}
	
	public boolean isValid(boolean validateTypeBinding)
	{
		if (values.getValues() == null ||
				values.getValues().isEmpty() ) {
			return false;
		}
		
		if(values.size() != typeRestrictions.size())
		{
			return false;
		}
		
		//TODO: otestovat moznosti typu - like this?:
		if(validateTypeBinding)
		{
			for(int i = 0; i < values.size(); i++)
			{
				if(!typeRestrictions.getByIndex(i).getTypes().contains(
						values.getValue(i).getType()))
				{
					return false;
				}
			}
		}
		
		for(Value value : values.getValues())
		{
			if(!value.isValid())
			{
				return false;
			}
		}
		
		return true;
	}

	public String exportToWeka() {
		
		String wekaString = "-" + this.getName();
		
		for (Value valueI : values.getValues()) {
			if (valueI.getTypedValue() instanceof NullValue) {
				continue;
			}

			wekaString += " " + valueI.getTypedValue().exportToWeka();
		}
		
		return wekaString;
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
