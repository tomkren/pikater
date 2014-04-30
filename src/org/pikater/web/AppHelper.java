package org.pikater.web;

import java.util.Set;

import org.pikater.web.config.ServerConfigurationInterface;
import org.reflections.Reflections;

public class AppHelper
{
	// ----------------------------------------------------------------------------------------------------------
	// PATHS TO RESOURCES
	
	public static final String baseAppPath = ServerConfigurationInterface.getContext().getRealPath("/");
	public static final String srcPath = joinPathComponents(baseAppPath, "src");
	public static final String webInfPath = joinPathComponents(baseAppPath, "WEB-INF");
	public static final String webInfConfPath = joinPathComponents(webInfPath, "conf");
	public static final String webInfJadeDeployPath = joinPathComponents(webInfPath, "jadeDeploy");
	public static final String webInfLibPath = joinPathComponents(webInfPath, "lib");
	
	// ----------------------------------------------------------------------------------------------------------
 	// PUBLIC INTERFACE
	
	/**
	 * @param prefixPath
	 * 		this path is required to end with a path separator
	 * @param fileNameOrDirectory
	 * 		this directory name must not end with a path separator
	 * @return
	 * 		the new path
	 */
	public static String joinPathComponents(String prefixPath, String fileNameOrDirectory)
	{
		return prefixPath + fileNameOrDirectory + System.getProperty("file.separator");
	}
	
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
