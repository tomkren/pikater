package org.pikater.web.config;

import java.io.File;
import java.util.Properties;

import org.pikater.shared.PropertiesHandler;

public class ServerConfiguration extends PropertiesHandler
{
	/*
	 * Property strings defined in the application configuration properties file. Have to be kept in sync :).
	 */
	
	private static final String property_allowDefaultAdminAccount = "allowDefaultAdminAccount";
	private static final String property_coreEnabled = "coreEnabled";
	private static final String property_forceUseTopologies = "forceUseTopologies";
	private static final String property_forceLeaveTopologies = "forceLeaveTopologies";
	
	/*
	 * Variables to be parsed from the configuration file.
	 */
	
	public final boolean defaultAdminAccountAllowed;
	public final boolean coreEnabled;
	public final String forceUseTopologies;
	public final String forceLeaveTopologies;
	
	public ServerConfiguration(File resource)
	{
		Properties prop = openProperties(resource);
		if(prop != null)
		{
			defaultAdminAccountAllowed = new Boolean(getProperty(prop, property_allowDefaultAdminAccount));
			coreEnabled = new Boolean(getProperty(prop, property_coreEnabled));
			forceUseTopologies = getProperty(prop, property_forceUseTopologies);
			forceLeaveTopologies = getProperty(prop, property_forceLeaveTopologies);
			
		}
		else
		{
			defaultAdminAccountAllowed = false;
			coreEnabled = false;
			forceUseTopologies = null;
			forceLeaveTopologies = null;
		}
	}
	
	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	public boolean isValid()
	{
		return true;
	}
}