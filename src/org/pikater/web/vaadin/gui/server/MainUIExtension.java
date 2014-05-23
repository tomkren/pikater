package org.pikater.web.vaadin.gui.server;

import java.util.logging.Level;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.web.WebAppLogger;
import org.pikater.web.vaadin.gui.PikaterUI;
import org.pikater.web.vaadin.gui.client.extensions.MainUIExtensionClientRpc;
import org.pikater.web.vaadin.gui.client.extensions.MainUIExtensionServerRpc;

import com.vaadin.server.AbstractExtension;

/**
 * Instances of this class act as a mediator between client and server in
 * the sense that custom messages and data structures are sent over it.
 */
public class MainUIExtension extends AbstractExtension
{
	private static final long serialVersionUID = 8278201529558658998L;
	
	private BoxInfoCollection boxDefinitions;
	
	public MainUIExtension()
	{
		this.boxDefinitions = null;
		registerRpc(new MainUIExtensionServerRpc()
		{
			private static final long serialVersionUID = -5824200287684658506L;
			
			@Override
			public void logWarning(String message)
			{
				WebAppLogger.log(Level.WARNING, message);
			}

			@Override
			public void logThrowable(String message, String throwableStackTrace)
			{
				WebAppLogger.log(Level.SEVERE, String.format("%s\n%s", message, throwableStackTrace));
			}

			@Override
			public void logUncaughtNativeClientException()
			{
				WebAppLogger.log(Level.SEVERE, "An uncaught native client exception has been thrown. Best to launch a thorough debug.");
			}
		});
	}
	
	/**
	 * Exposing the inherited API.
	 * @param mainUI
	 */
	public void extend(PikaterUI mainUI)
    {
        super.extend(mainUI);
    }
	
	public BoxInfoCollection getBoxDefinitions()
	{
		return boxDefinitions;
	}
	
	public void setBoxDefinitions(BoxInfoCollection boxDefinitions)
	{
		this.boxDefinitions = boxDefinitions;
		getRpcProxy(MainUIExtensionClientRpc.class).setBoxDefinitions(boxDefinitions);
	}
}
