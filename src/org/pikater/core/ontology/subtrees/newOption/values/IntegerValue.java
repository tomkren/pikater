package org.pikater.core.ontology.subtrees.newOption.values;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IComparableValueData;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class IntegerValue implements IComparableValueData
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

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public Integer hackValue()
	{
		return value;
	}
	@Override
	public IValueData clone()
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
	@Override
	public int compareTo(IComparableValueData o)
	{
		return hackValue().compareTo((Integer) o.hackValue());
	}
}
