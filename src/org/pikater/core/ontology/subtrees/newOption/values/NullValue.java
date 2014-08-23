package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class NullValue implements IValueData
{
	private static final long serialVersionUID = 4240750027791781820L;
	
	public NullValue() {}
	
	@Override
	public int hashCode()
	{
		return 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return true; // needed in {@link NewOption#mergeWith(IMergeable)}
	}
	@Override
	public Object hackValue()
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
	public IValueData clone()
	{
		return new NullValue();
	}
}