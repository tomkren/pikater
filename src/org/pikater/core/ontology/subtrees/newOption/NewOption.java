package org.pikater.core.ontology.subtrees.newOption;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.restriction.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.NullValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.typedValue.QuestionMarkSet;

import com.thoughtworks.xstream.XStream;

public class NewOption {

	private String name;
	private String description;
	
	private Values values;
	private boolean isMutable = false;
	
	private TypeRestriction possibleTypesRestriction;
	
	
	public NewOption() {
		this.values = new Values();
	}
	public NewOption(ITypedValue value, String name) {
		this.values = new Values();
		this.values.addValue(new Value(value));
		this.setName(name);
	}
	public NewOption(ITypedValue defaultValue, Type type, String name) {
		this.values = new Values();
		this.values.addValue( new Value(null, defaultValue, type) );
		this.setName(name);
	}
	public NewOption(List<ITypedValue> values, String name) {
		this.values = new Values();
		for (ITypedValue valueI : values) {
			this.getValuesWrapper().addValue( new Value(valueI) );
		}
		this.setName(name);
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

	public Values getValuesWrapper() {
		return values;
	}
	public void setValuesWrapper(Values values) {
		this.values = values;
	}
	public void resetValues(List<Value> values) {
		this.values.setValues(values);
	}
	
	public boolean isEmpty() {
		return values == null && values.getValues().size() == 0;
	}
	public boolean isSingleValue() {
		return values.getValues().size() == 1;
	}
	
	public boolean getIsMutable() {
		return isMutable;
	}
	public void setIsMutable(boolean isMutable) {
		this.isMutable = isMutable;
	}
	
	public TypeRestriction getPossibleTypesRestriction() {
		return possibleTypesRestriction;
	}
	public void setPossibleTypesRestriction(
			TypeRestriction possibleTypesRestriction) {
		this.possibleTypesRestriction = possibleTypesRestriction;
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
	public NewOption clone() {
		
		NewOption newOption = new NewOption();
		newOption.setName(getName());
		newOption.setDescription(getDescription());
		newOption.setIsMutable(this.getIsMutable());
		
		for (Value valueI : values.getValues()) {
			newOption.getValuesWrapper().addValue(valueI.clone());
		}
		newOption.setPossibleTypesRestriction(
				possibleTypesRestriction.clone());
		
		return newOption;
	}
	
	public boolean isValid(boolean validateTypeBinding)
	{
		if (values.getValues() == null ||
				values.getValues().isEmpty() ) {
			return false;
		}
		
		if(values.getValues().size() != possibleTypesRestriction.getPossibleTypes().size())
		{
			return false;
		}
		
		//TODO: otestovat moznosti typu - like this?:
		if(validateTypeBinding)
		{
			for(int i = 0; i < values.getValues().size(); i++)
			{
				if(!possibleTypesRestriction.getPossibleTypes().get(i).getTypes().contains(
						values.getValues().get(i).getType()))
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
