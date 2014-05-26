package org.pikater.core.ontology.subtrees.newOption.value;

public class BooleanValue implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 486219518827018753L;

	private boolean value;

	public BooleanValue() {}
	public BooleanValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}	

}
