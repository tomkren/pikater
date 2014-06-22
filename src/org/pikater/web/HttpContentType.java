package org.pikater.web;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public enum HttpContentType
{
	APPLICATION_JAR("application/java-archive", ".jar"),
	APPLICATION_OCTET_STREAM("application/octet-stream", ".bin"),
	APPLICATION_MS_EXCEL("application/vnd.ms-excel", ".xls"),
	APPLICATION_MS_OFFICE_OPEN_SPREADSHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
	APPLICATION_XHTML_XML("application/xhtml+xml", ".xhtml"),
	APPLICATION_XML("application/xml", ".xml"),
	TEXT_CSV("text/csv", ".csv"),
	TEXT_HTML("text/html", ".htm, .html"),
	TEXT_PLAIN("text/plain", ".txt"),
	WILDCARD("*/*", "any format");
	
	private final String contentType;
	private final String extensionList;
	
	private HttpContentType(String contentType, String extensionList)
	{
		this.contentType = contentType;
		this.extensionList = extensionList;
	}
	
	@Override
	public String toString()
	{
		return contentType;
	}
	
	public String getExtensionList()
	{
		return extensionList;
	}
	
	public static List<String> getMimeTypeList(EnumSet<HttpContentType> contentTypes)
	{
		List<String> result = new ArrayList<String>();
		Iterator<HttpContentType> iter = contentTypes.iterator();
		while (iter.hasNext())
		{
			result.add(iter.next().contentType);
		}
		return result;
	}
	
	public static String getExtensionList(EnumSet<HttpContentType> contentTypes)
	{
		StringBuilder result = new StringBuilder();
		Iterator<HttpContentType> iter = contentTypes.iterator();
		if(iter.hasNext())
		{
			result.append(iter.next().getExtensionList());
		}
		while(iter.hasNext())
		{
			result.append(", ").append(iter.next().getExtensionList());
		}
		return result.toString();
	}
}