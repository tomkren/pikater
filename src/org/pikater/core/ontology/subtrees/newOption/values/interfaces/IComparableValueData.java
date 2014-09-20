package org.pikater.core.ontology.subtrees.newOption.values.interfaces;

public interface IComparableValueData extends IValueData, Comparable<IComparableValueData>
{
	@Override
	public IComparableValueData clone();
}