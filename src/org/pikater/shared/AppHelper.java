package org.pikater.shared;

import java.util.Set;

import org.reflections.Reflections;

public class AppHelper
{
	public static final String baseAppPath = System.getProperty("user.dir") + System.getProperty("file.separator"); 
	public static final String srcPath = baseAppPath + "src" + System.getProperty("file.separator");
	
	public static String getPath(Class<?> clazz)
	{
		return srcPath + clazz.getPackage().getName().replace(".", "/") + System.getProperty("file.separator");
	}
	
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
