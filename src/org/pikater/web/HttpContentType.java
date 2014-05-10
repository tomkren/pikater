package org.pikater.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class HttpContentType
{
	// -------------------------------------------------------
	// PUBLIC FIELDS
	
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
	public static final String APPLICATION_JAR = "application/java-archive";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String WILDCARD = "*/*";
	
	// -------------------------------------------------------
	// PRIVATE FIELDS
	
	private static final Map<String, String> mimeTypeToExtMapping = new HashMap<String, String>();
	static
	{
		mimeTypeToExtMapping.put(APPLICATION_XML, ".xml");
		mimeTypeToExtMapping.put(APPLICATION_XHTML_XML, ".xhtml");
		mimeTypeToExtMapping.put(APPLICATION_JAR, ".jar");
		mimeTypeToExtMapping.put(APPLICATION_OCTET_STREAM, ".bin");
		mimeTypeToExtMapping.put(TEXT_HTML, ".htm, .html");
		mimeTypeToExtMapping.put(TEXT_PLAIN, ".txt");
		mimeTypeToExtMapping.put(WILDCARD, "any format");
	}
	
	// -------------------------------------------------------
	// PUBLIC METHODS
	
	public static String getExtensionListByMIMEType(String mimeType)
	{
		return mimeTypeToExtMapping.get(mimeType);
	}
	
	public static String getExtensionListByMIMEType(Collection<String> mimeTypes)
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(String mimeType : mimeTypes)
		{
			if(!first)
			{
				result.append(", ");
			}
			else
			{
				first = false;
			}
			result.append(getExtensionListByMIMEType(mimeType));
		}
		return result.toString();
	}
}
