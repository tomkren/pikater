package org.pikater.web.vaadin.gui.client.mainuiextension;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTLogger;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.server.MainUIExtension;

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
		registerRpc(MainUIExtensionClientRpc.class, new MainUIExtensionClientRpc()
		{
			private static final long serialVersionUID = 5749987507481194601L;
			
			@Override
			public void setBoxDefinitions(BoxInfoCollection boxDefinitions)
			{
				GWTMisc.setBoxDefinitions(boxDefinitions);
			}
		});
		
		// getConnection().
		// getParent()
	}
	
	@Override
	protected void extend(ServerConnector target)
	{
		GWTLogger.setRemoteLogger(serverRPC);
	}
}
