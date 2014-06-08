package org.pikater.core.ontology.subtrees.newOption.value;

public class DoubleValue implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1276470189024492227L;

	private double value;
	
	public DoubleValue() {}
	public DoubleValue(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

}
