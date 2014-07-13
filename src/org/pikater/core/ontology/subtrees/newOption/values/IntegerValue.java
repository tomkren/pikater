package org.pikater.core.ontology.subtrees.newOption.values;

public class IntegerValue implements ITypedValue
{
	private static final long serialVersionUID = -2925380308174903951L;

	private int value;

	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public IntegerValue() {}
	public IntegerValue(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public ITypedValue clone()
	{
		return new IntegerValue(value);
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
