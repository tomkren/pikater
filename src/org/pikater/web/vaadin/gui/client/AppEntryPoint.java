package org.pikater.web.vaadin.gui.client;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTLogger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Event;

public class AppEntryPoint implements EntryPoint
{
	@Override
	public void onModuleLoad()
	{
		/*
		 * Creates a special JS namespace, injects it into the window object and
		 * exports some JSNI functions that can be called from the server.
		 */
		// JSNI_SharedConfig.exportStaticMethods();

		/*
		 * Adds a keyboard listener that keeps track of what keys are currently
		 * pressed. The underlying code is called after an event is triggered
		 * and even before the browser processes it which allows for cancelling
		 * events.
		 */
		Event.addNativePreviewHandler(GWTKeyboardManager.getNativePreviewHandler());
		
		/*
		 * Send uncaught exceptions to the server:  
		 */
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
		{
			@Override
			public void onUncaughtException(Throwable e)
			{
				GWTLogger.logThrowable("Uncaught exception from client:", e);
				
				/*
				if((e instanceof UmbrellaException) || (e instanceof JavaScriptException))
				{
					// native exceptions usually contain no useful information => send a generic "warning" instead:
					GWTLogger.logUncaughtNativeClientException();
				}
				else
				{
					GWTLogger.logThrowable("Uncaught exception from client:", e);
				}
				*/
			}
		});
	}
}