package org.pikater.web.vaadin;

import org.pikater.web.vaadin.gui.server.components.upload.UserUploadsManager;

public class NoSessionStoreData
{
	public final UserUploadsManager uploadManager;

	public NoSessionStoreData()
	{
		this.uploadManager = new UserUploadsManager();
	}
}
