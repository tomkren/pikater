package org.pikater.web.config;

import javax.servlet.ServletContext;

public class ServerConfigurationInterface
{
	public enum ServerConfItem
	{
		CONTEXT,
		CONFIG,
		JADE_TOPOLOGIES,
		BOX_DEFINITIONS,
	};
	
	/**
	 * Unique keys to use for the attributes to store.
	 */
	private static ServletContext context = null;
	private static ServerConfiguration config = null;
	private static JadeTopologies jadeTopologies = null;
	private static AgentInfoCollection knownAgents = null;
	
	// **************************************************************************************************
	// PUBLIC INTERFACE
	
	public static void setField(ServerConfItem item, Object value)
	{
		switch (item)
		{
			case CONTEXT:
				if(context != null)
				{
					throw new IllegalStateException("Application context is already defined.");
				}
				else
				{
					context = (ServletContext) value;
				}
				break;
			case CONFIG:
				if(config != null)
				{
					throw new IllegalStateException("Application configuration is already defined.");
				}
				else
				{
					config = (ServerConfiguration) value;
				}
				break;
			case JADE_TOPOLOGIES:
				if(jadeTopologies != null)
				{
					throw new IllegalStateException("Known agents are already defined.");
				}
				else
				{
					jadeTopologies = (JadeTopologies) value;
				}
				break;
			case BOX_DEFINITIONS:
				knownAgents = (AgentInfoCollection) value;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public static ServletContext getContext()
	{
		return context;
	}
	
	public static ServerConfiguration getConfig()
	{
		return config;
	}
	
	public static JadeTopologies getJadeTopologies()
	{
		return jadeTopologies;
	}
	
	public static AgentInfoCollection getKnownAgents()
	{
		return knownAgents;
	}
	
	public static Boolean avoidUsingDBForNow()
	{
		return false; // TODO: move this to application configuration eventually
	}
	
	public static boolean isApplicationReadyToServe()
	{
		/* TODO: synchronize with full application configuration
		return (getContext() != null) && 
				(getConfig() != null) && getConfig().isValid() && 
				(getJadeTopologies() != null) && (getJadeTopologies().getConnectedTopologies().size() > 1);
				*/
		return true;
	}
}
