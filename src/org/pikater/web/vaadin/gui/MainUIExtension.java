package org.pikater.web.vaadin.gui;

import java.util.logging.Level;

import org.pikater.web.WebAppLogger;
import org.pikater.web.vaadin.gui.client.mainuiextension.MainUIExtensionServerRpc;

import com.vaadin.server.AbstractExtension;

public class MainUIExtension extends AbstractExtension
{
	private static final long serialVersionUID = 8278201529558658998L;
	
	private MainUIExtensionServerRpc rpc = new MainUIExtensionServerRpc()
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
	};
	
	public MainUIExtension()
	{
		registerRpc(rpc);
	}
	
    public void extend(PikaterUI mainUI)
    {
        super.extend(mainUI);
    }
}
