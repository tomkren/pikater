package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class BooleanValue implements IComparableValueData
{
	private static final long serialVersionUID = 486219518827018753L;

	private Boolean value;
	
	/**
	 * Should only be used by JADE.
	 */
	@Deprecated
	public BooleanValue() {}
	public BooleanValue(boolean value) {
		this.value = value;
	}
	
	public Boolean getValue()
	{
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
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
	
	@Override
	public int compareTo(IComparableValueData o)
	{
		return value.compareTo((Boolean) o.getValue());
	}
}