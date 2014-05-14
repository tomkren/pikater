package org.pikater.web.vaadin.gui.client.mainuiextension;

import com.vaadin.shared.communication.ServerRpc;

public interface MainUIExtensionServerRpc extends ServerRpc
{
	void logWarning(String message);
	void logThrowable(String message, String throwableStackTrace);
}
