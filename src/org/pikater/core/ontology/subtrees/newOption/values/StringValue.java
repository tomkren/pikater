package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class StringValue implements IComparableValueData
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
	public String hackValue()
	{
		return value;
	}
	@Override
	public IValueData clone()
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
	@Override
	public int compareTo(IComparableValueData o)
	{
		return hackValue().compareTo((String) o.hackValue());
	}
}