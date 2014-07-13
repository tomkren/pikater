package org.pikater.core.ontology.subtrees.newOption.values;

public class StringValue implements ITypedValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3094109600843562039L;

	private String value;
	
	/*
	 * Default constructor is needed.
	 */
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
	public ITypedValue clone() {

		StringValue valueNew = new StringValue();
		valueNew.setValue(value);
		
		return valueNew;
	}
	
	@Override
	public String exportToWeka() {
		
		return value;
	}
	
	@Override
	public String toDisplayName()
	{
		return "Text";
	}
}
