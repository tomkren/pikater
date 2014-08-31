package org.pikater.web.unused;

import javax.servlet.ServletContext;

import org.pikater.web.config.JadeTopologies;
import org.pikater.web.config.WebAppConfiguration;

/**
 * A wrapper class for all getters/setters of any server variables.
 */
public class ServerConfigurationInterfaceOld
{
	/**
	 * The configuration instance to be used throughout the whole application.
	 */
	public static WebAppConfiguration instance;
	
	/**
	 * Unique keys to use for the attributes to store.
	 */
	private static final String serverConfiguration = "config";
	private static final String serverConfigurationNotNull = "configNotNull";
	private static final String serverConfigurationValid = "configValid";
	
	private static final String jadeTopologies = "topologies";
	private static final String jadeTopologiesNotNull = "topologiesNotNull";
	private static final String jadeLaunchedAndRunning = "jadeRunning";
	
	private static final String everythingOk = "OK";
	
	/**
	 * Context to get and set attributes to/from.
	 */
	public final ServletContext context;
	
	public ServerConfigurationInterfaceOld(ServletContext ctx)
	{
		this.context = ctx;
		setObject(serverConfigurationNotNull, false);
		setObject(serverConfigurationValid, false);
		setObject(jadeTopologiesNotNull, false);
		setObject(jadeLaunchedAndRunning, false);
		setObject(everythingOk, false);
	}
	
	// **************************************************************************************************
	// PUBLIC INTERFACE
	
	public boolean isConfigurationSet()
	{
		return (Boolean) getObject(serverConfigurationNotNull);
	}
	
	public boolean isConfigurationValid()
	{
		return (Boolean) getObject(serverConfigurationValid);
	}
	
	public JadeTopologies getJadeTopologies()
	{
		return (JadeTopologies) getObject(jadeTopologies);
	}

	public void setJadeTopologies(JadeTopologies topologies)
	{
		if(topologies == null)
		{
			throw new NullPointerException();
		}
		else if(!isJadeSet()) // only set if it has not been set before
		{
			setObject(jadeTopologies, topologies);
			setObject(jadeTopologiesNotNull, true);
			checkAgain();
		}
		else
		{
			throw new IllegalStateException("Parsed Jade topologies have already been set once.");
		}
	}
	
	public boolean isJadeSet()
	{
		return (Boolean) getObject(jadeTopologiesNotNull);
	}
	
	public boolean isJadeLaunchedAndRunning()
	{
		return (Boolean) getObject(jadeLaunchedAndRunning);
	}
	
	public boolean isEverythingOK()
	{
		return (Boolean) getObject(everythingOk);
	}
	
	// **************************************************************************************************
	// PRIVATE INTERFACE

	private Object getObject(String label)
	{
		return context.getAttribute(label);
	}
	
	private void setObject(String label, Object object)
	{
		context.setAttribute(label, object);
	}
	
	private void checkAgain()
	{
		setObject(everythingOk, isConfigurationSet() && isConfigurationValid() && isJadeSet() && isJadeLaunchedAndRunning());
	}
}
