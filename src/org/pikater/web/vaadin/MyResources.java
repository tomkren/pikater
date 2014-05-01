package org.pikater.web.vaadin;

import java.io.File;

import org.pikater.shared.AppHelper;
import org.pikater.web.WebAppHelper;

import com.vaadin.server.FileResource;

public class MyResources
{
	// Downloadable files are usually provided by clicking a Link.
	// Resources can be both static (a downloadable WEB-INF file for instance) and dynamic (URL link needs to be constructed).
	// Stream resources allow creating dynamic resource content. Charts are typical examples of dynamic images.
	
	public static final FileResource prop_appConf = new FileResource(new File(AppHelper.joinPathComponents(WebAppHelper.webInfConfPath, "appServer.properties")));
}
