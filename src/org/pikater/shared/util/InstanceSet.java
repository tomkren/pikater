package org.pikater.shared.util;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * This implementation of the Set interface fundamentally differs from the standard Set implementations - it uses
 * instance comparison only (e.g. o1 == o2) instead of equals method comparison to determine whether 2 objects equal
 * each other.
 * While this breaks the Set interface conduct, it works, serves its purpose and allows the use of all standard Set methods
 * with no custom implementations whatsoever. 
 */
public class InstanceSet<E> implements Set<E>
{
	private final Set<E> instanceSet;
	
	public InstanceSet()
	{
		this.instanceSet = Collections.newSetFromMap(new IdentityHashMap<E,Boolean>());
	}
	
	public InstanceSet(InstanceSet<E> source)
	{
		this.instanceSet = Collections.newSetFromMap(new IdentityHashMap<E,Boolean>());
		this.instanceSet.addAll(source);
	}
	
	@Override
	public int size()
	{
		return instanceSet.size();
	}

	@Override
	public boolean isEmpty()
	{
		return instanceSet.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return instanceSet.contains(o);
	}

	@Override
	public Iterator<E> iterator()
	{
		return instanceSet.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return instanceSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return instanceSet.toArray(a);
	}

	@Override
	public boolean add(E e)
	{
		return instanceSet.add(e);
	}

	@Override
	public boolean remove(Object o)
	{
		return instanceSet.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return instanceSet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		return instanceSet.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return instanceSet.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return instanceSet.removeAll(c);
	}

	@Override
	public void clear()
	{
		instanceSet.clear();
	}
}
