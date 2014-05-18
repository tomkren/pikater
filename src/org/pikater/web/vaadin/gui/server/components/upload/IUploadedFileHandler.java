package org.pikater.web.vaadin.gui.server.components.upload;

import java.io.InputStream;

import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;

public interface IUploadedFileHandler extends UploadFinishedHandler
{
	/**
	 * Invoked when a file is successfully uploaded. It is stored as a temporary file on local file system
	 * and when this method returns, it gets deleted.
	 */
	@Override
	public void handleFile(InputStream streamToLocalFile, String fileName, String mimeType, long sizeInBytes);
}
