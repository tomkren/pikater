package org.pikater.web.vaadin;


public class NoSessionStoreData
{
	public final ManageUserUploads uploadManager;

	public NoSessionStoreData()
	{
		this.uploadManager = new ManageUserUploads();
	}
}
