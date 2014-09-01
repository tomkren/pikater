package org.pikater.web.vaadin;

import java.io.File;
import java.io.Serializable;
import java.util.EnumSet;

import org.pikater.shared.util.collections.CollectionUtils;
import org.pikater.web.HttpContentType;
import org.pikater.web.vaadin.gui.server.components.upload.IFileUploadEvents;
import org.pikater.web.vaadin.gui.server.components.upload.MyMultiUpload;
import org.pikater.web.vaadin.gui.server.components.upload.MyUploadStateWindow;

import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;

/**
 * A class wrapping multi file-upload functionality for a single user - an instance of this class
 * is exclusively mapped to a particular session. This allows easy integration and nice usage of
 * the MultiFileUpload add-on.
 */
public class ManageUserUploads implements Serializable, IFileUploadEvents
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
	
	public ManageUserUploads()
	{
		this.currentUploadsCount = 0;
		this.originalPushMode = null;
	}
	
	// -----------------------------------------------
	// INHERITED INTERFACE
	
	/**
	 * Only for internal use.
	 */
	@Override
	public synchronized void uploadStarted(StreamingStartEvent event)
	{
		if(!isAFileBeingUploaded())
		{
			onThingsGoingToBeUploaded();
		}
		currentUploadsCount++;
	}

	/**
	 * Only for internal use.
	 */
	@Override
	public synchronized void uploadFailed(StreamingErrorEvent event)
	{
		currentUploadsCount--;
		if(!isAFileBeingUploaded())
		{
			onThingsUploadFinished();
		}
	}

	/**
	 * Only for internal use.
	 */
	@Override
	public synchronized void uploadFinished(StreamingEndEvent event, File uploadedFileHandler)
	{
		currentUploadsCount--;
		if(!isAFileBeingUploaded())
		{
			onThingsUploadFinished();
		}
	}
	
	// -----------------------------------------------
	// OTHER PUBLIC METHODS
	
	public MyUploadStateWindow createUploadInfoProvider()
	{
		MyUploadStateWindow result = new MyUploadStateWindow();
		result.setWindowPosition(MyUploadStateWindow.WindowPosition.BOTTOM_RIGHT);
		return result;
	}
	
	/**
	 * This is the most important method of this class.
	 * Creates a new upload button and binds it with the constant UploadStateWindow instance, unique for each
	 * user (this class is unique for each user). 
	 * @param caption
	 * @param allowedMIMETypes
	 * @param customCallbacks
	 * @param uploadedFileHandler
	 * @return
	 */
	public MyMultiUpload createUploadButton(String caption, MyUploadStateWindow uploadInfoProvider, EnumSet<HttpContentType> allowedMIMETypes)
	{
		MyMultiUpload result = new MyMultiUpload(uploadInfoProvider, false); // true doesn't work... seems to be a bug in the plugin
		result.setMaxFileSize(oneGB);
		result.setAcceptedMimeTypes(HttpContentType.getMimeTypeList(allowedMIMETypes));
		result.setMimeTypeErrorMsgPattern(String.format("Error: you can only upload '%s' files via this dialog.", 
				CollectionUtils.join(HttpContentType.getExtensionList(allowedMIMETypes), ", ")));
		result.setSizeErrorMsgPattern("Error: you can only upload files up to 1GB.");
		result.setMaxVisibleRows(5);
		result.getSmartUpload().setUploadButtonCaptions(caption, caption);
		result.addFileUploadEventsCallback(this); // don't forget this... the callbacks defined here have to be called first (added first)
		return result;
	}

	/** 
	 * @return whether a file is being uploaded in ANY UI instance
	 */
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