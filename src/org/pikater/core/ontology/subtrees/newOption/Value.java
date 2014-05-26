package org.pikater.core.ontology.subtrees.newOption;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;

public class Value implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778874898115080831L;

	private Type type;

	private IValue value;
	private IValue defaultValue;


	public Value() {}
	public Value(IValue value) {
		this.value = value;
		this.type = new Type(value.getClass());
	}
	public Value(IValue value, Type type) {
		this.value = value;
		this.type = type;
	}
	public Value(IValue value, IValue defaultValue, Type type) {
		this.value = value;
		this.defaultValue = defaultValue;
		this.type = type;
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

		boolean typeOk = type.isValid();

		return valueOk && defaultValueOk && typeOk;
	}

}
