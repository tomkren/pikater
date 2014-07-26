package org.pikater.web.servlets.download.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.pikater.web.servlets.download.DownloadRegistrar.DownloadLifespan;

/**
 * A generic class wrapping files in downloadable resources.
 */
public class FileDownloadResource implements IDownloadResource
{
	private final File file;
	private final DownloadLifespan lifeSpan;
	private final String mimeType;
	
	public FileDownloadResource(File file, DownloadLifespan lifeSpan, String mimeType)
	{
		this.file = file;
		this.lifeSpan = lifeSpan;
		this.mimeType = mimeType;
	}
	
	@Override
	public DownloadLifespan getLifeSpan()
	{
		return lifeSpan;
	}

	@Override
	public String getFilename()
	{
		return file.getName();
	}
	
	@Override
	public String getMimeType()
	{
		return mimeType;
	}

	@Override
	public long getSize()
	{
		return file.length();
	}

	@Override
	public InputStream getStream() throws Throwable
	{
		return new FileInputStream(file);
	}
}