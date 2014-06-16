package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.ClientRpc;

public interface ExpEditorExtensionClientRpc extends ClientRpc
{
	/**
	 * These pictures will not be used directly in Vaadin but rather in the Kinetic environment
	 * on the client. When drawing boxes in the Kinetic stage, images need to be already loaded
	 * on the client (downloaded and ready), otherwise Kinetic won't draw them correctly.
	 * That is what this method aims to do.
	 * @param pictureURLs
	 */
	void command_loadBoxPictures(String[] pictureURLs);
	
	/**
	 * Command to change the size of boxes on the client.
	 * @param percent the percentage of width of the browser's viewport
	 */
	void command_setBoxSize(int percent);
}