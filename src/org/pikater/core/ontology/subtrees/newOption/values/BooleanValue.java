package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class BooleanValue implements IValueData
{
	private static final long serialVersionUID = 486219518827018753L;

	private boolean value;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public BooleanValue() {}
	public BooleanValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue()
	{
		return value;
	}
	public void setValue(boolean value) 
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
		result = prime * result + (value ? 1231 : 1237);
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
		BooleanValue other = (BooleanValue) obj;
		if (value != other.value)
			return false;
		return true;
	}
	//-------------------------------------------------------------
	// OTHER INHERITED INTERFACE
	@Override
	public Boolean hackValue()
	{
		return value;
	}
	@Override
	public IValueData clone()
	{
		return new BooleanValue(value);
	}
	@Override
	public String exportToWeka()
	{
		return String.valueOf(value);
	}
	@Override
	public String toDisplayName()
	{
		return "Boolean";
	}
}