package org.pikater.web.vaadin.gui.client.jsni;

public class JSNI_SharedConfig
{
	public static enum ApplicationMode
	{
		DEBUG,
		PRODUCTION
	};
	
	/**
	 * Call this from the main module's entry point to inject the desired JSNI functions into the window object.
	 */
	public static native void exportStaticMethods()
	/*-{
	 	// init
	 	var ns_pikater = ns_pikater || {};
	 	
	 	// just an example
	 	// ns_pikater.setAppMode = $entry(@org.pikater.web.vaadin.gui.client.jsni.JSNI_SharedConfig::setAppMode(Ljava/lang/String;));
	 	
	 	// inject
	 	$wnd.ns_pikater = ns_pikater;
	}-*/;
	
	// -----------------------------------------------------------
	// EXAMPLE APPLICATION MODE ROUTINES
	
	/**
	 * Used to set the application mode on the client.
	 * @param mode
	 */
	private static native void setAppMode(String mode)
	/*-{
 		$wnd.ns_pikater.appMode = mode;
	}-*/;
	
	private static native ApplicationMode getAppMode()
	/*-{
		return @org.pikater.web.vaadin.gui.client.jsni.JSNI_SharedConfig$ApplicationMode::valueOf(Ljava/lang/String;)($wnd.ns_pikater.appMode);
	}-*/;
	
	/*
	 * with(ns_pikater)
		{
   			setAppMode("DEBUG");
   			$wnd.setAppMode("DEBUG");
		}
	 */
	
	// -----------------------------------------------------------
	// TRUE APPLICATION MODE ROUTINES
	
	public static native boolean isDebugModeActivated()
	/*-{
		return $wnd.vaadin.debug;
	}-*/;
}
