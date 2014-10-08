package org.pikater.shared.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.util.ICloneable;

/**
 * Utility class providing various utility methods to collections.
 * 
 * @author SkyCrawl
 */
public class CollectionUtils
{
	/**
	 * Always returns a non-null list - either the argument list or
	 * a new empty list.
	 * 
	 * @param list for which we want safe access
	 * @return a non-null list
	 */
	public static <O extends Object> List<O> nullSafeList(List<O> list)
	{
		return list != null ? list : Collections.<O>emptyList();
	}
	
	public static Set<String> enumSetToStringSet(EnumSet<?> enumSet)
	{
		Set<String> result = new HashSet<String>();
		for(Enum<?> enumValue : enumSet)
		{
			result.add(enumValue.name());
		}
		return result;
	}
	
	public static Set<String> rangeToStringSet(int fromIncl, int toIncl)
	{
		Set<String> result = new LinkedHashSet<String>(); // LinkedHashSet preserves insertion order
		for(int i = fromIncl; i <= toIncl; i++)
		{
			result.add(String.valueOf(i));
		}
		return result;
	}
	
	public static String join(Collection<String> collection, String delim)
	{
		StringBuilder result = new StringBuilder();
		Iterator<String> it = collection.iterator();
		if(it.hasNext())
		{
			result.append(it.next());
		}
		while(it.hasNext())
		{
			result.append(delim);
			result.append(it.next());
		}
		return result.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <C extends ICloneable> List<C> deepCopy(List<C> source)
	{
		if(source == null)
		{
			return null;
		}
		else
		{
			List<C> copy = new ArrayList<C>(source.size());
			for(C item : source)
			{
				ICloneable itemCopy = ((ICloneable) item).clone();
				copy.add((C) itemCopy);
			}
			return copy;
		}
	}
}