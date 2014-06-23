package org.pikater.shared.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.pikater.shared.logging.PikaterLogger;

public class IOUtils
{
	//----------------------------------------------------------------------------------------------------------------
	// PATHS RELATED STUFF
	
	private static String baseAbsAppPath = null;
	
	public static void setAbsoluteBaseAppPath(String baseAbsAppPath)
	{
		IOUtils.baseAbsAppPath = baseAbsAppPath;
	}
	
	/**
	 * If you're going to use this, first use the {@link #setAbsoluteBaseAppPath setAbsoluteBaseAppPath} method.
	 * @return
	 */
	public static String getAbsoluteBaseAppPath()
	{
		if(baseAbsAppPath == null)
		{
			throw new IllegalStateException("The base application path has not been set.");
		}
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
	// FILE RELATED STUFF
		
	public static String readTextFile(String filePath)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable(String.format("Could not read the '%s' file because of the below IO error:", filePath), e);
			return null;
		}
	}
	
	public static void writeToFile(String filePath, String content, Charset charset)
	{
		try
		{
			Files.write(Paths.get(filePath), content.getBytes(charset), StandardOpenOption.CREATE_NEW);
		}
		catch (IOException e)
		{
			PikaterLogger.logThrowable(String.format("Could not write given content to file '%s' because of the below IO error:", filePath), e);
		}
	}
}