package org.pikater.web.vaadin.gui.client.config;

import org.pikater.web.vaadin.gui.client.mainuiextension.MainUIExtensionServerRpc;

public class GWTLogger
{
	private static MainUIExtensionServerRpc serverRPC = null;
	
	public static void setRemoteLogger(MainUIExtensionServerRpc serverRPC)
	{
		GWTLogger.serverRPC = serverRPC;
	}
	
	private static boolean checkLogger()
	{
		if(serverRPC == null)
		{
			throw new NullPointerException("Remote logger has not been set. Ensure that your main UI is extended with a remote logger. Everything else should be "
					+ "taken case of automatically.");
		}
		else
		{
			return true;
		}
	}
	
	private static String printThrowable(Throwable properJavaThrowable)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(properJavaThrowable.toString());
		sb.append('\n');
		for(StackTraceElement element : properJavaThrowable.getStackTrace())
		{
			sb.append("\tat " + element.toString());
			sb.append('\n');
		}
		if(properJavaThrowable.getCause() != null)
		{
			sb.append("caused by: ");
			sb.append(printThrowable(properJavaThrowable.getCause()));
		}
		return sb.toString();
	}
	
	// ----------------------------------------------------------
	// LOGGING METHODS
	
	/**
	 * Use this to send a warning message to the server - it will be logged there.
	 * @param message
	 */
	public static void logWarning(String message)
	{
		if(checkLogger())
		{
			serverRPC.logWarning(message);
		}
	}

	/**
	 * Only use this with your own Java exceptions in GWT code - they will be sent to the server and logged. 
	 * Don't use this with native exceptions - these have stack traces like "at http://localhost:8080/.../ijk:1500" and that's
	 * obviously useless.
	 * @param properJavaThrowable
	 * @return
	 */
	public static void logThrowable(String message, Throwable t)
	{
		if(checkLogger())
		{
			serverRPC.logThrowable(message, printThrowable(t));
		}
	}
}
