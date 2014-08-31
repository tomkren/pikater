package org.pikater.web.config;

import javax.servlet.ServletContext;

public class WebAppConfiguration
{
	/**
	 * Unique keys parameters stored in "web.xml".
	 */
	private static final String paramKey_coreEnabled = "coreEnabled";
	
	/**
	 * Inner variables.
	 */
	private static ServletContext context = null;
	// private static JadeTopologies jadeTopologies = null;
	
	// **************************************************************************************************
	// PUBLIC SETTERS
	
	public static void setContext(ServletContext context)
	{
		if(WebAppConfiguration.context != null)
		{
			throw new IllegalStateException("Application context is already defined.");
		}
		else
		{
			WebAppConfiguration.context = context;
		}
	}
	
	// **************************************************************************************************
	// OTHER PUBLIC INTERFACE
	
	public static boolean isCoreEnabled()
	{
		return Boolean.parseBoolean((String) getContextParam(paramKey_coreEnabled));
	}
	
	public static boolean avoidUsingDBForNow()
	{
		return false;
	}
	
	public static boolean isValid()
	{
		return context != null;
	}
	
	public static boolean isApplicationReadyToServe()
	{
		return isValid(); // when remote Jade launching is implemented, edit accordingly
	}
	
	// **************************************************************************************************
	// PRIVATE INTERFACE
	
	@SuppressWarnings("unchecked")
	private static <PT extends Object> PT getContextParam(String key)
	{
		return (PT) context.getInitParameter(paramKey_coreEnabled);
	}
}