package org.pikater.core.ontology.subtrees.newOption.values;

public class NullValue implements ITypedValue
{
	private static final long serialVersionUID = 4240750027791781820L;
	
	public NullValue() {}
	
	@Override
	public Object getValue()
	{
		return null;
	}

	@Override
	public String exportToWeka()
	{
		return "";
	}
	
	@Override
	public String toDisplayName()
	{
		return "NONE";
	}
	
	@Override
	public ITypedValue clone()
	{
		return new NullValue();
	}
}