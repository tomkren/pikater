package org.pikater.web.vaadin.gui.client;

import com.google.gwt.core.client.EntryPoint;

public class AppEntryPoint implements EntryPoint
{
	@Override
	public void onModuleLoad()
	{
		ClientVars.setPreviewBrowserEvents(); // adds a keyboard listener that keeps track of what keys are currently pressed
	}
}