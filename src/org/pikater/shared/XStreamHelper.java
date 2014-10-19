package org.pikater.shared;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.util.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * Various utility methods for the XStream technology.
 * 
 * @author SkyCrawl
 */
public class XStreamHelper {
	// -------------------------------------------------------------
	// INSTANCE CREATION

	/**
	 * Creates a {@link XStream} object that registers all given classes and
	 * processes their annotations. The returned object is suitable to be
	 * passed to other methods of this class or have its {@link XStream#toXML()}
	 * and {@link XStream#fromXML()} methods called right away.
	 * 
	 */
	public static XStream getSerializerWithProcessedAnnotations(Class<?>... annotationsToProcess) {
		XStream result = new XStream();
		for (Class<?> clazz : annotationsToProcess) {
			result.processAnnotations(clazz);
		}
		return result;
	}

	// -------------------------------------------------------------
	// PUBLIC INTERFACE

	/**
	 * By default, {@link XStream} doesn't add XML declaration to the result
	 * files. This method first writes:
	 * <code><pre>&lt?xml version="1.0" encoding="UTF-8" ?&gt</pre></code>
	 * 
	 * and then writes the object's XML representation.
	 * 
	 * @return XML representation of the object, with a proper XML declaration
	 */
	public static String serializeToXML(Object objectToSerialize, XStream serializer) {
		return String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>%s%s", System.getProperty("line.separator"), serializer.toXML(objectToSerialize));
	}

	/**
	 * Outputs the result of {@link #serializeToXML(Object, XStream)} to a file.
	 * 
	 * @throws IOException
	 */
	public static void serializeToFile(String filePath, Object objectToSerialize, XStream serializer) throws IOException {
		if (new File(filePath).isFile()) {
			throw new IOException("File already exists.");
		} else {
			IOUtils.writeToFile(filePath, serializeToXML(objectToSerialize, serializer), Charset.forName("UTF-8"));
		}
	}

	/**
	 * Deserializes an object with the given class to an object from the given
	 * file using the given deserializer.
	 * 
	 */
	public static <T> T deserializeFromPath(Class<T> clazz, String path, XStream deserializer) {
		return deserializeFromXML(clazz, IOUtils.readTextFile(path), deserializer);
	}

	/**
	 * Deserializes an object with the given class to an object from the given
	 * XML string representation using the given deserializer.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromXML(Class<T> clazz, String xml, XStream deserializer) {
		try {
			return (T) deserializer.fromXML(xml);
		} catch (XStreamException e) {
			PikaterDBLogger.logThrowable(String.format("Could not deserialize the following XML to the '%s' class.", clazz.getSimpleName()), e);
			return null;
		}
	}
}
