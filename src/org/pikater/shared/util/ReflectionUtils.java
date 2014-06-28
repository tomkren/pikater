package org.pikater.shared.util;

import java.util.Set;

import org.reflections.Reflections;

public class ReflectionUtils
{
	public static Set<Class<? extends Object>> getTypesFromPackage(Package pkg)
	{
		Reflections reflections = new Reflections(pkg.getName());
		return reflections.getSubTypesOf(Object.class);
	}
	
	public static <T> Set<Class<? extends T>> getSubtypesFromPackage(Package pkg, Class<T> ancestor)
	{
		Reflections reflections = new Reflections(pkg.getName());
		return reflections.getSubTypesOf(ancestor);
	}
	
	public static <T> Set<Class<? extends T>> getSubtypesFromSamePackage(Class<T> ancestor)
	{
		Reflections reflections = new Reflections(ancestor.getPackage().getName());
		return reflections.getSubTypesOf(ancestor);
	}
}