package org.pikater.web;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public enum HttpContentType
{
	APPLICATION_JAR("application/java-archive", ".jar"),
	APPLICATION_OCTET_STREAM("application/octet-stream", ".bin"),
	APPLICATION_MS_EXCEL("application/vnd.ms-excel", ".xls"),
	APPLICATION_MS_OFFICE_OPEN_SPREADSHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
	APPLICATION_XHTML_XML("application/xhtml+xml", ".xhtml"),
	APPLICATION_XML("application/xml", ".xml"),
	TEXT_CSV("text/csv", ".csv"),
	TEXT_HTML("text/html", ".htm", ".html"),
	TEXT_PLAIN("text/plain", ".txt"),
	WILDCARD("*/*");
	
	private final String contentType;
	private final String[] extensionList;
	
	private HttpContentType(String contentType, String... extensionList)
	{
		this.contentType = contentType;
		this.extensionList = extensionList;
	}
	
	public String getMimeType()
	{
		return contentType;
	}
	
	public String[] getExtensions()
	{
		return extensionList;
	}
	
	public boolean hasExtensionsDefined()
	{
		return (extensionList != null) && extensionList.length > 0; 
	}
	
	public static List<String> getMimeTypeList(EnumSet<HttpContentType> contentTypes)
	{
		List<String> result = new ArrayList<String>();
		Iterator<HttpContentType> iter = contentTypes.iterator();
		while (iter.hasNext())
		{
			result.add(iter.next().getMimeType());
		}
		return result;
	}
	
	public static Set<String> getExtensionList(EnumSet<HttpContentType> contentTypes)
	{
		Set<String> result = new LinkedHashSet<String>();
		Iterator<HttpContentType> iter = contentTypes.iterator();
		while(iter.hasNext())
		{
			for(String extension : iter.next().getExtensions())
			{
				result.add(extension);
			}
		}
		return result;
	}
	
	public static HttpContentType fromString(String contentType)
	{
		for(HttpContentType type : HttpContentType.values())
		{
			if(type.getMimeType().equalsIgnoreCase(contentType))
			{
				return type;
			}
		}
		return null;
	}
	
	//---------------------------------------------------------
	// SOME APPLICATION SPECIFIC INTERFACE
	
	public static EnumSet<HttpContentType> getDatasetUploadTypes()
	{
		return EnumSet.of(
				HttpContentType.APPLICATION_MS_EXCEL,
				HttpContentType.APPLICATION_MS_OFFICE_OPEN_SPREADSHEET, 
				HttpContentType.TEXT_CSV,
				HttpContentType.TEXT_PLAIN
		);
	}
}