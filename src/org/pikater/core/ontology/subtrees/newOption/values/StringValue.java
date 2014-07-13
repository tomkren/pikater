package org.pikater.core.ontology.subtrees.newOption.values;

public class StringValue implements ITypedValue
{
	private static final long serialVersionUID = 3094109600843562039L;

	private String value;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public StringValue() {}
	public StringValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	@Override
	public ITypedValue clone()
	{
		return new StringValue(value);
	}
	
	@Override
	public String exportToWeka()
	{
		return value;
	}
	
	@Override
	public String toDisplayName()
	{
		return "Text";
	}
}