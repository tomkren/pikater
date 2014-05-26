package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.web.vaadin.gui.MainUIExtension;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKineticSettings;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTLogger;

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
			public void command_setBoxDefinitions(BoxInfoCollection boxDefinitions)
			{
				GWTKineticSettings.setBoxDefinitions(boxDefinitions);
			}

			@Override
			public void command_setBoxSize(int percent)
			{
				GWTKineticSettings.setBoxSize(percent);
			}
		});
	}
	
	@Override
	protected void extend(ServerConnector target)
	{
		GWTLogger.setRemoteLogger(serverRPC);
	}
}
