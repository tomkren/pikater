package org.pikater.web.vaadin;

import java.io.File;

import org.pikater.shared.AppHelper;
import org.pikater.web.WebAppHelper;

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
	// INDIVIDUAL RESOURCES:
	
	public static final FileResource prop_appConf = new FileResource(new File(AppHelper.joinPathComponents(WebAppHelper.webInfConfPath, "appServer.properties")));
	
	public static final ThemeResource img_nextIcon16 = new ThemeResource("images/Arrow-Next-icon-16x16.png");
	public static final ThemeResource img_nextIcon20 = new ThemeResource("images/Arrow-Next-icon-20x20.png");
	public static final ThemeResource img_addIcon24 = new ThemeResource("images/add-icon-24x24.png");
	public static final ThemeResource img_plusIcon32 = new ThemeResource("images/Plus-icon-32x32.png");
}
