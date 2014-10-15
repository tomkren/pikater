package org.pikater.shared.util;

import java.util.Set;

import org.reflections.Reflections;

/**
 * Utility methods for java reflections technology.
 * 
 * @author SkyCrawl
 */
public class ReflectionUtils {
	/**
	 * Gets all subtypes of {@link Object} in the given package.
	 * @param pkg
	 * @return
	 */
	public static Set<Class<? extends Object>> getTypesFromPackage(Package pkg) {
		return getSubtypesFromPackage(pkg, Object.class);
	}

	/**
	 * Gets all subtypes of the given type in the given package.
	 * @param pkg
	 * @param ancestor
	 * @return
	 */
	public static <T> Set<Class<? extends T>> getSubtypesFromPackage(Package pkg, Class<T> ancestor) {
		Reflections reflections = new Reflections(pkg.getName());
		return reflections.getSubTypesOf(ancestor);
	}

	/**
	 * Gets all subtypes of the given type from the same package where it
	 * is defined. 
	 * @param ancestor
	 * @return
	 */
	public static <T> Set<Class<? extends T>> getSubtypesFromSamePackage(Class<T> ancestor) {
		Reflections reflections = new Reflections(ancestor.getPackage().getName());
		return reflections.getSubTypesOf(ancestor);
	}
}