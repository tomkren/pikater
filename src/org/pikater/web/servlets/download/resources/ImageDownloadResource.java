package org.pikater.web.servlets.download.resources;

import java.io.File;

import org.pikater.web.servlets.download.DownloadRegistrar.DownloadLifespan;

public class ImageDownloadResource extends FileDownloadResource
{
	private final int imageWidth;
	private final int imageHeight;
	
	public ImageDownloadResource(File file, DownloadLifespan lifeSpan, String mimeType, int imageWidth, int imageHeight) 
	{
		super(file, lifeSpan, mimeType);
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public int getImageWidth()
	{
		return imageWidth;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}
}