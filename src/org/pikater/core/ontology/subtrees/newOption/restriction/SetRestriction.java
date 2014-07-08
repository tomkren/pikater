package org.pikater.core.ontology.subtrees.newOption.restriction;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.value.NullValue;

public class SetRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 611963641797046162L;

	private List<ITypedValue> values;

	public SetRestriction() {}
	public SetRestriction(List<ITypedValue> values) {
		this.values = values;
	}

	public List<ITypedValue> getValues() {
		return values;
	}
	public void setValues(List<ITypedValue> values) {
		this.values = values;
	}
	public void addValue(ITypedValue value) {
		if (this.values == null) {
			this.values = new ArrayList<ITypedValue>();
		}
		
		this.values.add(value);
	}

	@Override
	public Type getClassName() {

		ITypedValue value0 = values.get(0);
		return new Type(value0.getClass());
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

	public boolean contains(ITypedValue value) {

		for (ITypedValue valueI : values) {
			if (valueI.equals(value)) {
				return true;
			}
		}

		return false;
	}
	
	public SetRestriction cloneSetRestriction() {
		
		SetRestriction setRest = new SetRestriction();
		for (ITypedValue valueI : values) {
			setRest.addValue(valueI.cloneValue());
		}
		return setRest;
	}

}
