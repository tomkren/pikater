package org.pikater.web.vaadin.gui.client.mainuiextension;

import org.pikater.web.vaadin.gui.MainUIExtension;
import org.pikater.web.vaadin.gui.client.config.GWTLogger;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(MainUIExtension.class)
public class MainUIExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 6766120104518020715L;
	
	private final MainUIExtensionServerRpc serverRPC = RpcProxy.create(MainUIExtensionServerRpc.class, this);

	public MainUIExtensionConnector()
	{
		/*
		registerRpc(MainUIExtensionClientRpc.class,
				new MainUIExtensionClientRpc()
				{
					public void alert(String message)
					{
						// TODO Do something useful
						Window.alert(message);
					}
				});
		*/
		
		// getConnection().
		// getParent()
	}
	
	@Override
	protected void extend(ServerConnector target)
	{
		GWTLogger.setRemoteLogger(serverRPC);
		// GWTLogger.logThrowable("something", new NullPointerException("YAY"));
	}
}
