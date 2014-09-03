package org.pikater.web.vaadin;

import java.util.logging.Level;

import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.web.vaadin.gui.client.extensions.UniversalUIExtensionClientRpc;
import org.pikater.web.vaadin.gui.client.extensions.UniversalUIExtensionServerRpc;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.UI;

public class UniversalUIExtension extends AbstractExtension
{
	private static final long serialVersionUID = 8278201529558658998L;
	
	public UniversalUIExtension()
	{
		registerRpc(new UniversalUIExtensionServerRpc()
		{
			private static final long serialVersionUID = -5824200287684658506L;
			
			@Override
			public void logWarning(String message)
			{
				PikaterLogger.log(Level.WARNING, message);
			}

			@Override
			public void logThrowable(String message, String throwableStackTrace)
			{
				PikaterLogger.log(Level.SEVERE, String.format("%s\n%s", message, throwableStackTrace));
			}

			@Override
			public void logUncaughtNativeClientException()
			{
				PikaterLogger.log(Level.SEVERE, "An uncaught native client exception has been thrown. Best to launch a thorough debug.");
			}
		});
	}
	
	/**
	 * Exposing the inherited API.
	 * @param mainUI
	 */
	public void extend(UI anyUI)
    {
        super.extend(anyUI);
    }
	
	public UniversalUIExtensionClientRpc getClientRPC()
	{
		return getRpcProxy(UniversalUIExtensionClientRpc.class); 
	}
}
