package org.pikater.shared.util;

import java.util.Set;

import org.reflections.Reflections;

public class ReflectionUtils
{
	public static <T> Set<Class<? extends T>> getSubtypesFromSamePackage(Class<T> clazz)
	{
		Reflections reflections = new Reflections(clazz.getPackage().getName());
		return reflections.getSubTypesOf(clazz);
	}
	
	public static Set<Class<? extends Object>> getTypesFromPackage(Package pkg)
	{
		Reflections reflections = new Reflections(pkg.getName());
		return reflections.getSubTypesOf(Object.class);
	}
}