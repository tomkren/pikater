package org.pikater.web.sharedresources.download;

import java.io.InputStream;

import org.pikater.web.sharedresources.IRegistrarResource;

public interface IDownloadResource extends IRegistrarResource
{
	String getFilename();
	String getMimeType();
	long getSize();
	InputStream getStream() throws Exception;
}