package org.pikater.web.config;

import java.util.Properties;

import org.pikater.web.resources.PropertiesHandler;

import com.vaadin.server.FileResource;

public class ServerConfiguration extends PropertiesHandler
{
	/*
	 * Property strings defined in the application configuration properties file. Have to be kept in sync :).
	 */
	private static final String property_allowDefaultAdminAccount = "allowDefaultAdminAccount";
	private static final String property_forceUseTopologies = "forceUseTopologies";
	private static final String property_forceLeaveTopologies = "forceLeaveTopologies";
	
	/*
	 * Variables to be parsed from the configuration file.
	 */
	public final boolean defaultAdminAccountAllowed;
	
	public final String forceUseTopologies;
	public final String forceLeaveTopologies;
	
	public ServerConfiguration(FileResource resource)
	{
		Properties prop = openProperties(resource);
		if(prop != null)
		{
			defaultAdminAccountAllowed = new Boolean(getProperty(resource.getFilename(), prop, property_allowDefaultAdminAccount));
			forceUseTopologies = getProperty(resource.getFilename(), prop, property_forceUseTopologies);
			forceLeaveTopologies = getProperty(resource.getFilename(), prop, property_forceLeaveTopologies);
		}
		else
		{
			defaultAdminAccountAllowed = false;
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
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
}
