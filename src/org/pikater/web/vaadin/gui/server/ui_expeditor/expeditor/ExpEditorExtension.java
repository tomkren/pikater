package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor;

import org.pikater.web.vaadin.gui.client.extensions.ExpEditorExtensionClientRpc;
import org.pikater.web.vaadin.gui.client.extensions.ExpEditorExtensionServerRpc;

import com.vaadin.server.AbstractExtension;

public class ExpEditorExtension extends AbstractExtension
{
	private static final long serialVersionUID = 8278201529558658998L;
	
	public ExpEditorExtension()
	{
		registerRpc(new ExpEditorExtensionServerRpc()
		{
			private static final long serialVersionUID = 216446786216335413L;
		});
	}
	
	/**
	 * Exposing the inherited API.
	 * @param mainUI
	 */
	public void extend(ExpEditor expEditor)
    {
        super.extend(expEditor);
    }
	
	public ExpEditorExtensionClientRpc getClientRPC()
	{
		return getRpcProxy(ExpEditorExtensionClientRpc.class); 
	}
}
