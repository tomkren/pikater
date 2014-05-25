package org.pikater.core.ontology.subtrees.newOption;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;

public class Value implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778874898115080831L;

	private Type type;

	private IValue value;
	private IValue defaultValue;

	private RangeRestriction rangeRestriction;
	private SetRestriction setRestriction;


	public Value() {}
	public Value(IValue value) {
		this.type = new Type(value.getClass());
		this.value = value;
	}

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public IValue getValue() {
		return value;
	}
	public void setValue(IValue value) {
		this.value = value;
	}

	public IValue getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(IValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	public RangeRestriction getRangeRestriction() {
		return rangeRestriction;
	}
	public void setRangeRestriction(RangeRestriction rangeRestriction) {
		this.rangeRestriction = rangeRestriction;
	}

	public SetRestriction getSetRestriction() {
		return setRestriction;
	}
	public void setSetRestriction(SetRestriction setRestriction) {
		this.setRestriction = setRestriction;
	}

	@Override
	public Type getClassName() {
		return this.type;
	}

	@Override
	public boolean isValid() {
		
		boolean valueOk = true;
		if (value != null && type != null) {
			valueOk = type.equals( new Type(value.getClass()) );
		}

		boolean defaultValueOk = true;
		if (defaultValue != null && type != null) {
			defaultValueOk = type.equals( new Type(defaultValue.getClass()) );
		}

		boolean rangeOk = rangeRestriction.isValid();

		boolean setOk = setRestriction.isValid() &&
				setRestriction.contains(value);


		return valueOk && defaultValueOk && rangeOk && setOk;
	}

}
