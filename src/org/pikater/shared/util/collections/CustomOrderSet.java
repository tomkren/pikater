package org.pikater.shared.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class CustomOrderSet<T> extends TreeSet<T>
{
	private static final long serialVersionUID = -7363544833191937694L;
	
	/**
	 * Creates an empty set sorting with natural order (the element type has
	 * to implement {@link Comparable}).
	 */
	public CustomOrderSet()
	{
		super();
	}
	
	/**
	 * Creates an empty set sorting with the given comparator.
	 */
	public CustomOrderSet(Comparator<T> comp)
	{
		super(comp);
	}

	/**
	 * Creates a duplicate of the given collection that sorts using natural
	 * ordering (the element type has to implement {@link Comparable}). 
	 */
	public CustomOrderSet(Collection<T> unsortedCollection)
	{
		super(unsortedCollection);
	}

	/**
	 * Creates a duplicate of the given collection that sorts using the given comparator.
	 */
	public CustomOrderSet(Collection<T> unsortedCollection, Comparator<T> comp)
	{
		super(comp);
		addAll(unsortedCollection);
	}
}
