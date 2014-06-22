package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKineticSettings;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditorExtension;

import com.google.gwt.user.client.ui.Image;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ExpEditorExtension.class)
public class ExpEditorExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 6766120104518020715L;
	
	// private final ExpEditorExtensionServerRpc serverRPC = RpcProxy.create(ExpEditorExtensionServerRpc.class, this);

	public ExpEditorExtensionConnector()
	{
		registerRpc(ExpEditorExtensionClientRpc.class, new ExpEditorExtensionClientRpc()
		{
			private static final long serialVersionUID = 560120982576334694L;
			
			@Override
			public void command_loadBoxPictures(final String[] pictureURLs)
			{
				// built-in image widget does exactly what we want, we only need to create an instance
				final Image[] imageWidgets = new Image[pictureURLs.length];
				for(int i = 0; i < pictureURLs.length; i++)
				{
					imageWidgets[i] = new Image(pictureURLs[i]);
				}
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
		// TODO: move GWTKineticSettings into this class?
	}
}
