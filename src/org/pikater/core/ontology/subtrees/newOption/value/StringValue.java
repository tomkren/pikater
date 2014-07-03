package org.pikater.core.ontology.subtrees.newOption.value;

public class StringValue implements IValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3094109600843562039L;

	private String value;
	
	public StringValue() {}
	public StringValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public IValue cloneValue() {

		StringValue valueNew = new StringValue();
		valueNew.setValue(value);
		
		return valueNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return value;
	}
	
}
