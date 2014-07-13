package org.pikater.core.ontology.subtrees.newOption.restrictions;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;

public class SetRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 611963641797046162L;

	private List<ITypedValue> values;

	public SetRestriction() {
		this.values = new ArrayList<ITypedValue>();
	}
	public SetRestriction(List<ITypedValue> values) {
		this.values = new ArrayList<ITypedValue>();
		addAllValues(values);
	}

	public List<ITypedValue> getValues() {
		return values;
	}
	public void setValues(List<ITypedValue> values) {
		this.values = values;
	}
	public void addValue(ITypedValue value) {
		if (value == null) {
			throw new IllegalArgumentException("Argument value can't be null");
		}
		
		this.values.add(value);
	}
	public void addAllValues(List<ITypedValue> values) {
		
		this.values.addAll(values);
	}

	@Override
	public ValueType getType() {

		ITypedValue value0 = values.get(0);
		return new ValueType(value0.getClass());
	}

	@Override
	public boolean isValid() {
		
		if (values == null || values.isEmpty()) {
			return false;
		}
		
		// check if all values are instances of same class
		ITypedValue value0 = new NullValue();
		for (ITypedValue valueI : values) {
			if (valueI.getClass() != NullValue.class) {
				value0 = valueI;
			}
		}
		for (ITypedValue valueI : values) {
			if (valueI.getClass() == NullValue.class) {
				continue;
			}
			if (valueI.getClass() != value0.getClass()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public SetRestriction clone() {
		
		SetRestriction setRest = new SetRestriction();
		for (ITypedValue valueI : values) {
			setRest.addValue(valueI.clone());
		}
		return setRest;
	}

}
