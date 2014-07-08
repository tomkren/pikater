package org.pikater.core.ontology.subtrees.newOption;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;

public class Value implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778874898115080831L;

	private Type type;

	private ITypedValue value;
	private ITypedValue defaultValue;


	public Value() {}
	public Value(ITypedValue value) {
		this.value = value;
		this.type = new Type(value.getClass());
	}
	public Value(ITypedValue value, Type type) {
		this.value = value;
		this.type = type;
	}
	public Value(ITypedValue value, ITypedValue defaultValue, Type type) {
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

	public ITypedValue getValue() {
		return value;
	}
	public void setValue(ITypedValue value) {
		this.value = value;
	}

	public ITypedValue getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(ITypedValue defaultValue) {
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

	public Value cloneValue() {
		
		Value valueNew = new Value();
		valueNew.setType(type.cloneType());
		valueNew.setValue(value.cloneValue());
		valueNew.setDefaultValue(defaultValue.cloneValue());
		
		return valueNew;
	}
}
