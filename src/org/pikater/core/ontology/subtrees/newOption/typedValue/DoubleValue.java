package org.pikater.core.ontology.subtrees.newOption.typedValue;

public class DoubleValue implements ITypedValue {

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
	
	@Override
	public ITypedValue cloneValue() {
		
		DoubleValue valueNew = new DoubleValue();
		valueNew.setValue(value);
		
		return valueNew;
	}

	@Override
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
}
