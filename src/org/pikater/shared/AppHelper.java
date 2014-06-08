package org.pikater.shared;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.logging.PikaterLogger;
import org.reflections.Reflections;

public class AppHelper
{
	//----------------------------------------------------------------------------------------------------------------
	// APPLICATION PATHS RELATED STUFF
	
	private static String baseAbsAppPath = null; // if you're going to use this, first set this field when the application starts
	
	public static void setAbsoluteBaseAppPath(String baseAbsAppPath)
	{
		AppHelper.baseAbsAppPath = baseAbsAppPath;
	}
	
	public static String getAbsoluteBaseAppPath()
	{
		return baseAbsAppPath;
	}
	
	public static String getAbsoluteSRCPath()
	{
		return joinPathComponents(getAbsoluteBaseAppPath(), "src");
	}
	
	public static String getAbsoluteCorePath()
	{
		return joinPathComponents(getAbsoluteBaseAppPath(), "core");
	}
	
	public static String getAbsoluteWEBINFPath()
	{
		return joinPathComponents(getAbsoluteBaseAppPath(), "WEB-INF");
	}
	
	public static String getAbsoluteWEBINFCLASSESPath()
	{
		return joinPathComponents(getAbsoluteWEBINFPath(), "classes");
	}
	
	public static String getAbsoluteWEBINFCONFPath()
	{
		return joinPathComponents(getAbsoluteWEBINFPath(), "conf");
	}
	
	/**
	 * Abstract method to join string paths. Automatically handles directory separation. The argument paths are assumed to be valid.
	 * @param prefixPath
	 * @param fileNameOrDirectory Must not be an absolute path.
	 * @return Path that is a concatenation of the arguments.
	 */
	public static String joinPathComponents(String prefixPath, String fileNameOrDirectory)
	{
		StringBuilder result = new StringBuilder();
		result.append(prefixPath);
		if(!prefixPath.endsWith(System.getProperty("file.separator")))
		{
			result.append(System.getProperty("file.separator"));
		}
		if(fileNameOrDirectory.startsWith(System.getProperty("file.separator")))
		{
			throw new IllegalArgumentException("The suffix path must not be absolute.");
		}
		result.append(fileNameOrDirectory);
		return result.toString();
	}
	
	public static String getAbsolutePath(String basePath, Class<?> clazz)
	{
		return joinPathComponents(basePath, clazz.getPackage().getName().replace(".", System.getProperty("file.separator")))
				+ System.getProperty("file.separator"); 
	}
	
	//----------------------------------------------------------------------------------------------------------------
	// OTHER PUBLIC ROUTINES
	
	public static String readTextFile(String path)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable(String.format("Could not deserialize the '%s' file because of the below IO error:", path), e);
			return null;
		}
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
		Set<String> result = new HashSet<String>();
		for(int i = fromIncl; i <= toIncl; i++)
		{
			result.add(String.valueOf(i));
		}
		return result;
	}
}
