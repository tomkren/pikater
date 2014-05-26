package org.pikater.core.ontology.subtrees.newOption.restriction;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.value.NullValue;

public class SetRestriction implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 611963641797046162L;

	private List<IValue> values;

	public SetRestriction() {}
	public SetRestriction(List<IValue> values) {
		this.values = values;
	}

	public List<IValue> getValues() {
		return values;
	}
	public void setValues(List<IValue> values) {
		this.values = values;
	}
	public void addValue(IValue value) {
		if (this.values == null) {
			this.values = new ArrayList<IValue>();
		}
		
		this.values.add(value);
	}

	@Override
	public Type getClassName() {

		IValue value0 = values.get(0);
		return new Type(value0.getClass());
	}

	@Override
	public boolean isValid() {
		
		if (values == null || values.isEmpty()) {
			return false;
		}
		
		// check if all values are instances of same class
		IValue value0 = new NullValue();
		for (IValue valueI : values) {
			if (valueI.getClass() != NullValue.class) {
				value0 = valueI;
			}
		}
		for (IValue valueI : values) {
			if (valueI.getClass() == NullValue.class) {
				continue;
			}
			if (valueI.getClass() != value0.getClass()) {
				return false;
			}
		}
		return true;
	}

	public boolean contains(IValue value) {

		for (IValue valueI : values) {
			if (valueI.equals(value)) {
				return true;
			}
		}

		return false;
	}

}
