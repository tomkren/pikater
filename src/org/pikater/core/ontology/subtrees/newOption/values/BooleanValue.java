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
	public void setValue(boolean value) {
		this.value = value;
	}
	
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
	public String exportToWeka() {
		
		return String.valueOf(value);
	}
	
	@Override
	public String toDisplayName()
	{
		return "Boolean";
	}
}