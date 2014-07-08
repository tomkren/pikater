package org.pikater.core.ontology.subtrees.newOption;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
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
	
	private PossibleTypesRestriction possibleTypesRestriction;
	
	
	public NewOption() {}
	public NewOption(ITypedValue value, String name) {
		
		this.addValue(new Value(value));
		this.setName(name);
	}
	public NewOption(ITypedValue defaultValue, Type type, String name) {
		
		this.addValue( new Value(null, defaultValue, type) );
		this.setName(name);
	}
	public NewOption(List<ITypedValue> values, String name) {
		
		for (ITypedValue valueI : values) {
			this.addValue( new Value(valueI) );
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

	public Values getValues() {
		return values;
	}
	public void setValues(Values values) {
		this.values = values;
	}
	public void resetValues(List<Value> values) {
		this.values.setValues(values);
	}
	public List<Value> returnValues() {
		return this.values.getValues();
	}
	
	public boolean isEmpty() {
		return values == null && values.getValues().size() == 0;
	}
	public boolean isSingleValue() {
		return values.getValues().size() == 1;
	}
	public void addValue(Value value) {
		if (this.values == null) {
			this.values = new Values();
		}
		values.addValue(value);
	}
	
	public boolean getIsMutable() {
		return isMutable;
	}
	public void setIsMutable(boolean isMutable) {
		this.isMutable = isMutable;
	}
	
	public PossibleTypesRestriction getPossibleTypesRestriction() {
		return possibleTypesRestriction;
	}
	public void setPossibleTypesRestriction(
			PossibleTypesRestriction possibleTypesRestriction) {
		this.possibleTypesRestriction = possibleTypesRestriction;
	}
	
	public Value convertToSingleValue() {
		if (this.values == null) {
			throw new IllegalStateException("Field values can't be null");
		}
		
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

	public NewOption cloneOption() {
		
		NewOption newOption = new NewOption();
		newOption.setName(getName());
		newOption.setDescription(getDescription());
		newOption.setIsMutable(this.getIsMutable());
		
		for (Value valueI : values.getValues()) {
			newOption.addValue(valueI.cloneValue());
		}
		newOption.setPossibleTypesRestriction(
				possibleTypesRestriction.clonePossibleTypesRestriction());
		
		return newOption;
	}
	
	public boolean isValid() {

		if (values.getValues() == null ||
				values.getValues().isEmpty() ) {
			return false;
		}
		
		//TODO: otestovat moznosti typu
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
