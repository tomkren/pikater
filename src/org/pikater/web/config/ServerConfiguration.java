package org.pikater.web.config;

import java.util.Properties;

import org.pikater.web.resources.PropertiesHandler;

import com.vaadin.server.FileResource;

public class ServerConfiguration extends PropertiesHandler
{
	/*
	 * Property strings defined in the application configuration properties file. Have to be kept in sync :).
	 */
	private static final String property_dbHostame = "dbHostname";
	private static final String property_dbPort = "dbPort";
	private static final String property_allowDefaultAdminAccount = "allowDefaultAdminAccount";
	private static final String property_forceUseTopologies = "forceUseTopologies";
	private static final String property_forceLeaveTopologies = "forceLeaveTopologies";
	
	/*
	 * Variables to be parsed from the configuration file.
	 */
	public final String dbHostname;
	public final int dbPort;
	
	public final boolean defaultAdminAccountAllowed;
	
	public final String forceUseTopologies;
	public final String forceLeaveTopologies;
	
	public ServerConfiguration(FileResource resource)
	{
		Properties prop = openProperties(resource);
		if(prop != null)
		{
			dbHostname = getProperty(resource.getFilename(), prop, property_dbHostame);
			dbPort = Integer.parseInt(getProperty(resource.getFilename(), prop, property_dbPort));
			defaultAdminAccountAllowed = new Boolean(getProperty(resource.getFilename(), prop, property_allowDefaultAdminAccount));
			forceUseTopologies = getProperty(resource.getFilename(), prop, property_forceUseTopologies);
			forceLeaveTopologies = getProperty(resource.getFilename(), prop, property_forceLeaveTopologies);
		}
		else
		{
			dbHostname = null; // IMPORTANT: don't change this lightly... it is required by validation (see below).
			dbPort = -1;
			defaultAdminAccountAllowed = false;
			forceUseTopologies = null;
			forceLeaveTopologies = null;
		}
	}
	
	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	public boolean isValid()
	{
		// IMPORTANT: 'dbHostname' check must come first (see above)!
		return (dbHostname != null) && (dbPort > 0);
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
}
