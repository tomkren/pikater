package org.pikater.shared;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

public class XStreamHelper
{
	public static <T> String serialize(T object)
	{
		XStream xstream = new XStream();
		return xstream.toXML(object); 
	}
	
	public static <T> T deserializeFromPath(String path) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		String xml = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		return deserializeFromXML(xml);
	}
	
	public static <T> T deserializeFromXML(String xml)
	{
		XStream xstream = new XStream();
		return (T) xstream.fromXML(xml);
	}
}
