package org.pikater.web.servlets.download;

import java.io.InputStream;

public interface IDownloadResource
{
	String getFilename();
	String getMimeType();
	long getSize();
	InputStream getStream() throws Throwable;
}
