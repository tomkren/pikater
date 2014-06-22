package org.pikater.web.vaadin.gui.server.components.upload.handlers;

import java.io.InputStream;

public class UploadedDatasetHandler implements IUploadedFileHandler
{
	/**
	 * This information may not always be provided. It is especially meant to be used
	 * when we upload a file that is not an ARFF file.
	 */
	private String customARFFHeaders;
	
	public UploadedDatasetHandler()
	{
		this.customARFFHeaders = null;
	}
	
	public void setARFFHeaders(String customARFFHeaders)
	{
		this.customARFFHeaders = customARFFHeaders;
	}

	@Override
	public void handleFile(InputStream streamToLocalFile, String fileName, String mimeType, long sizeInBytes)
	{
		// TODO: implement me
	}
}