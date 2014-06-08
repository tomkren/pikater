package org.pikater.web.vaadin.gui.client.extensions;

import com.vaadin.shared.communication.ServerRpc;

public interface UniversalUIExtensionServerRpc extends ServerRpc
{
	void logWarning(String message);
	void logThrowable(String message, String throwableStackTrace);
	void logUncaughtNativeClientException();
}
