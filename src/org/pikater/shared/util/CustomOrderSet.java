package org.pikater.shared.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class CustomOrderSet<T> extends TreeSet<T>
{
	private static final long serialVersionUID = -7363544833191937694L;
	
	/**
	 * Natural ordering instead of custom one.
	 * @param unsortedCollection
	 */
	public CustomOrderSet(Collection<T> unsortedCollection)
	{
		super(unsortedCollection);
	}

	/**
	 * Custom ordering.
	 * @param unsortedCollection
	 * @param comp
	 */
	public CustomOrderSet(Collection<T> unsortedCollection, Comparator<T> comp)
	{
		super(comp);
		addAll(unsortedCollection);
	}
}