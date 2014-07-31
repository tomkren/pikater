package org.pikater.web.visualisation.definition;

import com.google.common.net.MediaType;

public enum ImageType
{
	PNG(MediaType.PNG, ".png"),
	SVG(MediaType.SVG_UTF_8, ".svg");
	
	private final String mimeType;
	private final String extension;
	
	private ImageType(MediaType mimeType, String extension)
	{
		this.mimeType = mimeType.toString();
		this.extension = extension;
	}

	public String toMimeType()
	{
		return mimeType;
	}
	
	public String toFileExtension()
	{
		return extension;
	}
}