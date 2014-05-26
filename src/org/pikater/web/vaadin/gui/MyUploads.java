package org.pikater.web.vaadin.gui;

import java.io.Serializable;
import java.util.List;

import org.pikater.web.HttpContentType;
import org.pikater.web.vaadin.gui.server.components.upload.MyMultiUpload;
import org.pikater.web.vaadin.gui.server.components.upload.MyUploadStateWindow;

/**
 * A class wrapping multi-file upload functionality. A single instance is meant to be used in each vaadin session - when
 * a user access the page, an instance of this class is created and stored in the session. This particular manner of use
 * allows for easy integration and nice usage of the MultiFileUpload add-on.
 */
public class MyUploads implements Serializable
{
	private static final long serialVersionUID = 1643776529186934301L;
	
	// -----------------------------------------------
	// FILE SIZE DEFINITIONS
	
	/*
	 * Signed 4B-integer poses a maximum of 2.147 GB.
	 * Unsigned 4B-integer poses a maximum of 4.294 GB.
	 */
	
	private static final int oneKB = 1000;
	private static final int oneMB = oneKB * 1000;
	private static final int oneGB = oneMB * 1000;
	
	// -----------------------------------------------
	// INTERNAL FIELDS
	
	/**
	 * One upload state window for each user. If he issues uploads from multiple upload components,
	 * he will still see a single upload state window with all his issued uploads.
	 */
	private final MyUploadStateWindow stateWindow;
	
	/**
	 * The class providing information about how many files are currently being uploaded.
	 */
	private final UploadState uploadState;
	
	// -----------------------------------------------
	// CONSTRUCTOR
	
	public MyUploads()
	{
		this.stateWindow = new MyUploadStateWindow();
		this.stateWindow.setWindowPosition(MyUploadStateWindow.WindowPosition.BOTTOM_RIGHT);
		this.uploadState = new UploadState();
	}
	
	// -----------------------------------------------
	// PUBLIC METHODS
	
	public MyMultiUpload getNewComponent(List<String> allowedMIMETypes, IUploadedFileHandler uploadedFileHandler)
	{
		MyMultiUpload result = new MyMultiUpload(stateWindow, uploadedFileHandler, uploadState);
		result.setMaxFileSize(oneGB);
		result.setAcceptedMimeTypes(allowedMIMETypes);
		result.setMimeTypeErrorMsgPattern(String.format("Error: you can only upload '%s' files via this dialog.", HttpContentType.getExtensionListByMIMEType(allowedMIMETypes)));
		result.setSizeErrorMsgPattern("Error: you can only upload files up to 1GB.");
		result.setMaxVisibleRows(5);
		result.getSmartUpload().setUploadButtonCaptions("Single upload", "Multi-upload");
		return result;
	}
}