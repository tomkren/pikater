package org.pikater.core.ontology.subtrees.newOption.values;

public class IntegerValue implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2925380308174903951L;

	private int value;

	/*
	 * Default constructor is needed.
	 */
	public IntegerValue() {}
	public IntegerValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public ITypedValue clone() {

		IntegerValue valueNew = new IntegerValue();
		valueNew.setValue(value);
		
		return valueNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
	
	@Override
	public String toDisplayName()
	{
		return "Integer";
	}
}
