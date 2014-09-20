package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;

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
	
	/* -------------------------------------------------------------
	 * CUSTOM INSTANCE COMPARING - GENERATED WITH ECLIPSE
	 * - generate again when you change local fields or their types
	 * - required in {@link org.pikater.web.vaadin.gui.server.components.forms.OptionValueForm}
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringValue other = (StringValue) obj;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public String hackValue()
	{
		return value;
	}
	@Override
	public StringValue clone()
	{
		StringValue result;
		try
		{
			result = (StringValue) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setValue(value);
		return result;
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