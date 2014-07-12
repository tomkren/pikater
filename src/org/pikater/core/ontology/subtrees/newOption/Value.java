package org.pikater.core.ontology.subtrees.newOption;

import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.restriction.IRestriction;

public class Value implements IRestriction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778874898115080831L;

	private Type type;

	private ITypedValue typedValue;
	private ITypedValue defaultValue;


	public Value() {}
	public Value(ITypedValue typedValue) {
		this.typedValue = typedValue;
		this.type = new Type(typedValue.getClass());
	}
	public Value(ITypedValue value, Type type) {
		this.typedValue = value;
		this.type = type;
	}
	public Value(ITypedValue typedValue, ITypedValue defaultValue, Type type) {
		this.typedValue = typedValue;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public ITypedValue getTypedValue() {
		return typedValue;
	}
	public void setTypedValue(ITypedValue typedValue) {
		this.typedValue = typedValue;
	}

	public ITypedValue getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(ITypedValue defaultValue) {
		this.defaultValue = defaultValue;
	}

	
	@Override
	public boolean isValid() {
		
		boolean valueOk = true;
		if (typedValue != null && type != null) {
			valueOk = type.equals( new Type(typedValue.getClass()) );
		}

		boolean defaultValueOk = true;
		if (defaultValue != null && type != null) {
			defaultValueOk = type.equals( new Type(defaultValue.getClass()) );
		}

		boolean typeOk = type.isValid();

		return valueOk && defaultValueOk && typeOk;
	}

	@Override
	public Value clone() {
		
		Value valueNew = new Value();
		valueNew.setType(type.clone());
		valueNew.setTypedValue(typedValue.cloneValue());
		valueNew.setDefaultValue(defaultValue.cloneValue());
		
		return valueNew;
	}
}
