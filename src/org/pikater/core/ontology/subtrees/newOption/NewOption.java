package org.pikater.core.ontology.subtrees.newOption;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;

public class NewOption {

	private String name;
	private String description;
	
	private List<Value> values;
	
	private PossibleTypesRestriction possibleTypesRestriction;
	
	
	public NewOption() {}
	public NewOption(IValue value, String name) {
		
		this.addValue(new Value(value));
		this.setName(name);
	}
	public NewOption(IValue defaultValue, Type type, String name) {
		
		this.addValue( new Value(null, defaultValue, type) );
		this.setName(name);
	}
	public NewOption(List<IValue> values, String name) {
		
		for (IValue valueI : values) {
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

	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	public boolean isEmpty() {
		return values == null && values.size() == 0;
	}
	public boolean isSingleValue() {
		return values.size() == 1;
	}
	public void addValue(Value value) {
		if (this.values == null) {
			this.values = new ArrayList<Value>();
		}
		values.add(value);
	}

	public PossibleTypesRestriction getPossibleTypesRestriction() {
		return possibleTypesRestriction;
	}
	public void setPossibleTypesRestriction(
			PossibleTypesRestriction possibleTypesRestriction) {
		this.possibleTypesRestriction = possibleTypesRestriction;
	}
	public boolean isValid() {

		if (values == null || values.isEmpty() ) {
			return false;
		}
		
		//TODO: otestovat moznosti typu
		return true;
	}

}
