package org.pikater.web.vaadin.gui.client;

import org.pikater.web.vaadin.gui.client.config.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.jsni.JSNI_SharedConfig;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Event;

public class AppEntryPoint implements EntryPoint
{
	@Override
	public void onModuleLoad()
	{
		/*
		 * Creates a special JS namespace, injects it into the window object and exports some JSNI functions
		 * that can be called from the server.
		 */
		JSNI_SharedConfig.exportStaticMethods();
		
		/*
		 * Adds a keyboard listener that keeps track of what keys are currently pressed. The underlying
		 * code is called after an event is triggered and even before the browser processes it which allows
		 * for cancelling events.
		 */
		Event.addNativePreviewHandler(GWTKeyboardManager.getNativePreviewHandler());
	}
}