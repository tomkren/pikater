package org.pikater.core.ontology.subtrees.newOption.value;

public class FloatValue implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6154392916809467193L;
	
	private float value;
	
	public FloatValue() {}
	public FloatValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	@Override
	public IValue cloneValue() {
		
		FloatValue valueNew = new FloatValue();
		valueNew.setValue(value);
		
		return valueNew;
	}

	@Override
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
}
