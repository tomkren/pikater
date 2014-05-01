package org.pikater.web;

import org.pikater.shared.AppHelper;
import org.pikater.web.config.ServerConfigurationInterface;

public class WebAppHelper
{
	// ----------------------------------------------------------------------------------------------------------
	// PATHS TO RESOURCES
	
	public static final String baseAppPath = ServerConfigurationInterface.getContext().getRealPath("/");
	public static final String webInfPath = AppHelper.joinPathComponents(baseAppPath, "WEB-INF");
	public static final String webInfConfPath = AppHelper.joinPathComponents(webInfPath, "conf");
	
	// ----------------------------------------------------------------------------------------------------------
 	// PUBLIC INTERFACE
}
