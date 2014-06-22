package org.pikater.web.vaadin.gui.server.components.upload;

import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;

/**
 * Interface useful to implement if you wish to allow the user to continue browsing even 
 * before all uploads finish.</br></br>
 * 
 * <font color="red">THE PROBLEM: </font> All parent components or UIs of upload buttons created with the
 * {@link org.pikater.web.vaadin.ManageUserUploads#createUploadButton createUploadButton}
 * method has to be attached for as long as an upload is in progress, otherwise the upload is interrupted
 * and fails.</br></br>
 * 
 * <font color="red">THE SOLUTION: </font> don't detach the parent components, only hide them, e.g.
 * {@link com.vaadin.ui.AbstractComponent#setVisible setVisible(false)}.
 *  
 */
public interface IFileUploadEvents
{
	/**
	 * Whenever an upload starts.
	 */
	void uploadStarted(StreamingStartEvent event);
	
	/**
	 * Whenever an upload fails.
	 */
	void uploadFailed(StreamingErrorEvent event);
	
	/**
	 * When an upload is successfully finished. 
	 * @param event
	 */
	void uploadFinished(StreamingEndEvent event);
}
