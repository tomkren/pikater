package org.pikater.shared;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
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
		Set<String> result = new LinkedHashSet<String>(); // LinkedHashSet preserves insertion order
		for(int i = fromIncl; i <= toIncl; i++)
		{
			result.add(String.valueOf(i));
		}
		return result;
	}
	
	//Source: http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	public static String formatFileSize(long size){
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String formatInteger(Locale locale,int value){
		NumberFormat numberFormat=NumberFormat.getInstance(locale);
		return numberFormat.format(value);
	}
	
	public static String formatDouble(Locale locale,double value){
		NumberFormat numberFormat=NumberFormat.getInstance(locale);
		return numberFormat.format(value);
	}
	
	public static String formatBool(Locale locale,boolean value){
		return ""+value;
	}
	
	public static Locale getDefaultLocale(){
		return new Locale.Builder()
				.setLanguage("en")
				.setRegion("US")
				.build();
	}
	
}
