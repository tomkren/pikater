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
	
	// ----------------------------------------------------------------
	// THEME RESOURCE DEFINITIONS:
	
	public static final String relPath_IMG_nextIcon16 = "images/Arrow-Next-icon-16x16.png";
	public static final String relPath_IMG_plusIcon16 = "images/Plus-icon-16x16.png";
	public static final String relPath_IMG_closeIcon16 = "images/close-icon-16x16.png";
	
	// ----------------------------------------------------------------
	// RESOURCE FIELDS:
	
	public static final FileResource prop_appConf = new FileResource(new File(AppHelper.joinPathComponents(AppHelper.getAbsoluteWEBINFCONFPath(), "appServer.properties")));
	
	public static final ThemeResource img_nextIcon16 = new ThemeResource(relPath_IMG_nextIcon16);
	public static final ThemeResource img_plusIcon16 = new ThemeResource(relPath_IMG_plusIcon16);
	public static final ThemeResource img_closeIcon16 = new ThemeResource(relPath_IMG_closeIcon16);
	
	// ----------------------------------------------------------------
	// PUBLIC METHODS:
	
	public static String getVaadinRelativePathForResource(String relativeResourcePath)
	{
		return "./VAADIN/themes/pikater/" + relativeResourcePath;
	}
}
