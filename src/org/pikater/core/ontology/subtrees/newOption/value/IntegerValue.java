package org.pikater.core.ontology.subtrees.newOption.value;

public class IntegerValue implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2925380308174903951L;

	private int value;

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
	public ITypedValue cloneValue() {

		IntegerValue valueNew = new IntegerValue();
		valueNew.setValue(value);
		
		return valueNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
}
