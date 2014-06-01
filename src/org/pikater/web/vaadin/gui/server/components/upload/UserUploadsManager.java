package org.pikater.web.vaadin.gui.server.components.upload;

import java.io.Serializable;
import java.util.List;

import org.pikater.web.HttpContentType;
import org.pikater.web.vaadin.gui.server.components.upload.uploadedfilehandler.IUploadedFileHandler;

import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;

/**
 * A class wrapping multi file-upload functionality for a single user - an instance of this class
 * is exclusively mapped to a particular session. This allows easy integration and nice usage of
 * the MultiFileUpload add-on.
 */
public class UserUploadsManager implements Serializable
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
	 * How many files the corresponding user uploads at the moment.
	 */
	private int currentUploadsCount;
	
	/**
	 * A backup of the original push mode. When an upload starts, set the current UI's push mode to
	 * Automatic and when all the uploads finish, set it back to the original value. 
	 */
	private PushMode originalPushMode;
	
	// -----------------------------------------------
	// CONSTRUCTOR
	
	public UserUploadsManager()
	{
		this.stateWindow = new MyUploadStateWindow();
		this.stateWindow.setWindowPosition(MyUploadStateWindow.WindowPosition.BOTTOM_RIGHT);
		this.currentUploadsCount = 0;
		this.originalPushMode = null;
	}
	
	// -----------------------------------------------
	// PUBLIC METHODS
	
	public MyMultiUpload getNewComponent(List<String> allowedMIMETypes, IUploadedFileHandler uploadedFileHandler)
	{
		MyMultiUpload result = new MyMultiUpload(stateWindow, uploadedFileHandler, null); // TODO:
		result.setMaxFileSize(oneGB);
		result.setAcceptedMimeTypes(allowedMIMETypes);
		result.setMimeTypeErrorMsgPattern(String.format("Error: you can only upload '%s' files via this dialog.", HttpContentType.getExtensionListByMIMEType(allowedMIMETypes)));
		result.setSizeErrorMsgPattern("Error: you can only upload files up to 1GB.");
		result.setMaxVisibleRows(5);
		result.getSmartUpload().setUploadButtonCaptions("Single upload", "Multi-upload");
		return result;
	}

	public synchronized void onStreamingStart()
	{
		if(!isAFileBeingUploaded())
		{
			onThingsGoingToBeUploaded();
		}
		currentUploadsCount++;
	}
	
	public synchronized void onStreamingEnd()
	{
		currentUploadsCount--;
		if(!isAFileBeingUploaded())
		{
			onThingsUploadFinished();
		}
	}
	
	public boolean isAFileBeingUploaded()
	{
		return get() > 0;
	}
	
	// -----------------------------------------------
	// PRIVATE FIELDS
	
	private synchronized int get()
	{
		return currentUploadsCount;
	}
	
	private void onThingsGoingToBeUploaded()
	{
		originalPushMode = UI.getCurrent().getPushConfiguration().getPushMode();
		UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		// alternative: polling (e.g. "UI.setPollInterval(1500);")
	}
	
	private void onThingsUploadFinished()
	{
		UI.getCurrent().getPushConfiguration().setPushMode(originalPushMode);
	}
}