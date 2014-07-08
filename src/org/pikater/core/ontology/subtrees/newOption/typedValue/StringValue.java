package org.pikater.core.ontology.subtrees.newOption.typedValue;

public class StringValue implements ITypedValue {

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
	public ITypedValue cloneValue() {

		StringValue valueNew = new StringValue();
		valueNew.setValue(value);
		
		return valueNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return value;
	}
	
}
