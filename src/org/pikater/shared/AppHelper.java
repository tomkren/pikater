package org.pikater.shared;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.pikater.web.WebAppLogger;
import org.reflections.Reflections;

public class AppHelper
{
	public static final String baseAppPath = System.getProperty("user.dir");
	public static final String srcPath = joinPathComponents(baseAppPath, "src");
	public static final String corePath = joinPathComponents(baseAppPath, "core");
	
	public static String readTextFile(String path)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		}
		catch (IOException e)
		{
			WebAppLogger.logThrowable(String.format("Could not deserialize the '%s' file because of the below IO error:", path), e);
			return null;
		}
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
