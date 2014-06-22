package org.pikater.shared;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.pikater.shared.logging.PikaterLogger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

public class XStreamHelper
{
	// -------------------------------------------------------------
	// INSTANCE CREATION
	
	public static XStream getSerializerWithProcessedAnnotations(Class<?>... annotationsToProcess)
	{
		XStream result = new XStream();
		for(Class<?> clazz : annotationsToProcess)
		{
			result.processAnnotations(clazz);
		}
		return result;
	}
	
	// -------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public static String serializeToXML(Object objectToSerialize, XStream serializer)
	{
		return String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>%s%s", System.getProperty("line.separator"), serializer.toXML(objectToSerialize));
	}
	
	public static void serializeToFile(String filePath, Object objectToSerialize, XStream serializer) throws IOException
	{
		if(new File(filePath).isFile())
		{
			throw new IOException("File already exists.");
		}
		else
		{
			AppHelper.writeToFile(filePath, serializeToXML(objectToSerialize, serializer), Charset.forName("UTF-8"));
		}
	}
	
	public static <T> T deserializeFromPath(Class<T> clazz, String path, XStream deserializer)
	{
		return deserializeFromXML(clazz, AppHelper.readTextFile(path), deserializer);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromXML(Class<T> clazz, String xml, XStream deserializer)
	{
		try
		{
			return (T) deserializer.fromXML(xml);
		}
		catch (XStreamException e)
		{
			PikaterLogger.logThrowable(String.format("Could not deserialize the following XML to the '%s' class.", clazz.getSimpleName()), e);
			return null;
		}
	}
}
