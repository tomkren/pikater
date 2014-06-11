package org.pikater.web.vaadin;

import java.io.File;

import org.pikater.shared.AppHelper;

import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;

public class MyResources
{
	/*
	 * GENERAL NOTES:
	 * - Resources can be both static (a downloadable WEB-INF file for instance) and dynamic (URL link needs to be constructed).
	 * - Stream resources allow creating dynamic resource content. Charts are typical examples of dynamic images.
	 * - ThemeResource will look for the file in "VAADIN/themes/pikater/" (see example below).
	 */
	
	// ----------------------------------------------------------------
	// NOTABLE SERVER-RELATED TYPES:
		
	// com.vaadin.server.FileDownloader
	// com.vaadin.server.ExternalResource
	// com.vaadin.server.StreamResource
	// Link - a link to internal or external resource - can open a new window
	
	// ----------------------------------------------------------------
	// THEME RESOURCE DEFINITIONS:
	
	/*
	 * Banner.
	 */
	public static final String relPath_IMG_banner = "images/banner.png";
	
	/*
	 * Notification icons.
	 */
	public static final String relPath_IMG_notificationInfoIcon = "images/Win8MetroIcons/icon-info-48x48.png";
	public static final String relPath_IMG_notificationSuccessIcon = "images/Win8MetroIcons/icon-success-48x48.png";
	public static final String relPath_IMG_notificationWarnIcon = "images/Win8MetroIcons/icon-warn-48x48.png";
	public static final String relPath_IMG_notificationErrorIcon = "images/Win8MetroIcons/icon-error-48x48.png";
	
	/*
	 * Various.
	 */
	public static final String relPath_IMG_checkIcon16 = "images/check-16x16.png";
	public static final String relPath_IMG_clearIcon16 = "images/clear-16x16.png";
	public static final String relPath_IMG_nextIcon16 = "images/Arrow-Next-icon-16x16.png";
	public static final String relPath_IMG_plusIcon16 = "images/Plus-icon-16x16.png";
	public static final String relPath_IMG_closeIcon16 = "images/close-icon-16x16.png";
	public static final String relPath_IMG_minimizeIcon16 = "images/minimize-icon-16x16.png";
	
	// ----------------------------------------------------------------
	// RESOURCE FIELDS:
	
	public static final FileResource prop_appConf = new FileResource(new File(AppHelper.joinPathComponents(AppHelper.getAbsoluteWEBINFCONFPath(), "appServer.properties")));
	
	/*
	 * Banner.
	 */
	public static final ThemeResource img_banner = new ThemeResource(relPath_IMG_banner);
	
	/*
	 * Notifications icons.
	 */
	public static final ThemeResource img_notificationInfoIcon = new ThemeResource(relPath_IMG_notificationInfoIcon);
	public static final ThemeResource img_notificationSuccessIcon = new ThemeResource(relPath_IMG_notificationSuccessIcon);
	public static final ThemeResource img_notificationWarnIcon = new ThemeResource(relPath_IMG_notificationWarnIcon);
	public static final ThemeResource img_notificationErrorIcon = new ThemeResource(relPath_IMG_notificationErrorIcon);
	
	/*
	 * Various.
	 */
	public static final ThemeResource img_checkIcon16 = new ThemeResource(relPath_IMG_checkIcon16);
	public static final ThemeResource img_clearIcon16 = new ThemeResource(relPath_IMG_clearIcon16);
	public static final ThemeResource img_nextIcon16 = new ThemeResource(relPath_IMG_nextIcon16);
	public static final ThemeResource img_plusIcon16 = new ThemeResource(relPath_IMG_plusIcon16);
	public static final ThemeResource img_closeIcon16 = new ThemeResource(relPath_IMG_closeIcon16);
	public static final ThemeResource img_minimizeIcon16 = new ThemeResource(relPath_IMG_minimizeIcon16);
	
	// ----------------------------------------------------------------
	// PUBLIC METHODS:
	
	public static String getVaadinRelativePathForResource(String relativeResourcePath)
	{
		return "./VAADIN/themes/pikater/" + relativeResourcePath;
	}
}
