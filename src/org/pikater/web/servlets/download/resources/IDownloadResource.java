package org.pikater.web.servlets.download.resources;

import java.io.InputStream;

import org.pikater.web.servlets.download.DownloadRegistrar.DownloadLifespan;

public interface IDownloadResource
{
	DownloadLifespan getLifeSpan();
	String getFilename();
	String getMimeType();
	long getSize();
	InputStream getStream() throws Throwable;
}
