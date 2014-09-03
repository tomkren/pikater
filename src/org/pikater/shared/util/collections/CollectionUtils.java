package org.pikater.shared.util.collections;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class CollectionUtils
{
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
}