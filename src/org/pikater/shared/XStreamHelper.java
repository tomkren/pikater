package org.pikater.shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.pikater.web.AppLogger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

public class XStreamHelper
{
	// -------------------------------------------------------------
	// INSTANCE CREATION
	
	public static final XStream topologySerializer;
	static
	{
		topologySerializer = new XStream();
		topologySerializer.processAnnotations(TopologyModel.class);
	}
	
	// -------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public static void serializeToFile(String filePath, Object objectToSerialize, XStream serializer) throws IOException
	{
		if(new File(filePath).isFile())
		{
			throw new IOException("File already exists.");
		}
		else
		{
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("UTF-8"));
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
			serializer.toXML(objectToSerialize, writer);
		}
	}
	
	public static <T> T deserializeFromPath(Class<T> clazz, String path, XStream deserializer)
	{
		try
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			String xml = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
			return deserializeFromXML(clazz, xml, deserializer);
		}
		catch (IOException e)
		{
			AppLogger.logThrowable(String.format("Could not deserialize the '%s' file because of the below IO error:", path), e);
			return null;
		}
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
			AppLogger.logThrowable(String.format("Could not deserialize the following XML to the '%s' class.", clazz.getSimpleName()), e);
			return null;
		}
	}
}
