package org.pikater.web.vaadin;

import java.io.File;

import org.pikater.web.AppHelper;

import com.vaadin.server.FileResource;

public class MyResources
{
	// Downloadable files are usually provided by clicking a Link.
	// Resources can be both static (a downloadable WEB-INF file for instance) and dynamic (URL link needs to be constructed).
	// Stream resources allow creating dynamic resource content. Charts are typical examples of dynamic images.
	
	public static final FileResource exampleResource = new FileResource(new File(AppHelper.joinPathComponents(AppHelper.webInfLibPath, "jade.jar")));
	public static final FileResource prop_appConf = new FileResource(new File(AppHelper.joinPathComponents(AppHelper.webInfConfPath, "appServer.properties")));
}
