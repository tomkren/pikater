package org.pikater.web.vaadin.gui.server;

import org.pikater.web.vaadin.gui.server.components.upload.UserUploadManager;

public class NoSessionStoreData
{
	public final UserUploadManager uploadManager;

	public NoSessionStoreData()
	{
		this.uploadManager = new UserUploadManager();
	}
}
