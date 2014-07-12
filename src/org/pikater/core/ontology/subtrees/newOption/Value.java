package org.pikater.core.ontology.subtrees.newOption;

import jade.content.Concept;

import org.pikater.core.ontology.subtrees.newOption.valuetypes.ITypedValue;

public class Value implements Concept
{
	private static final long serialVersionUID = 4778874898115080831L;

	private ValueType type;

	private ITypedValue typedValue;
	private ITypedValue defaultValue;


	public Value() {}
	public Value(ITypedValue typedValue) {
		this.typedValue = typedValue;
		this.type = new ValueType(typedValue.getClass());
	}
	public Value(ITypedValue value, ValueType type) {
		this.typedValue = value;
		this.type = type;
	}
	public Value(ITypedValue typedValue, ITypedValue defaultValue, ValueType type) {
		this.typedValue = typedValue;
		this.defaultValue = defaultValue;
		this.type = type;
	}

	public ValueType getType() {
		return this.type;
	}
	public void setType(ValueType type) {
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

	
	public boolean isValid() {
		
		boolean valueOk = true;
		if (typedValue != null && type != null) {
			valueOk = type.equals( new ValueType(typedValue.getClass()) );
		}

		boolean defaultValueOk = true;
		if (defaultValue != null && type != null) {
			defaultValueOk = type.equals( new ValueType(defaultValue.getClass()) );
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
