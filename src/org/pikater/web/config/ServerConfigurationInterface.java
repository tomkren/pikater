package org.pikater.web.config;

import javax.servlet.ServletContext;

import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.web.vaadin.gui.client.extensions.MainUIExtensionClientRpc;

public class ServerConfigurationInterface
{
	public enum ServerConfItem
	{
		CONTEXT,
		CONFIG,
		JADE_TOPOLOGIES,
		BOX_DEFINITIONS,
		UNIVERSAL_CLIENT_CONNECTOR
	};
	
	/**
	 * Unique keys to use for the attributes to store.
	 */
	private static ServletContext context = null;
	private static ServerConfiguration config = null;
	private static JadeTopologies jadeTopologies = null;
	private static BoxInfoCollection boxDefinitions = null;
	private static MainUIExtensionClientRpc universalClientConnector = null;
	
	// **************************************************************************************************
	// PUBLIC INTERFACE
	
	public static void setField(ServerConfItem item, Object value)
	{
		switch (item)
		{
			case CONTEXT:
				if(context != null)
				{
					throw new IllegalStateException();
				}
				else
				{
					context = (ServletContext) value;
				}
				break;
			case CONFIG:
				if(config != null)
				{
					throw new IllegalStateException();
				}
				else
				{
					config = (ServerConfiguration) value;
				}
				break;
			case JADE_TOPOLOGIES:
				if(jadeTopologies != null)
				{
					throw new IllegalStateException();
				}
				else
				{
					jadeTopologies = (JadeTopologies) value;
				}
				break;
			case BOX_DEFINITIONS:
				boxDefinitions = (BoxInfoCollection) value;
				if(getUniversalClientConnector() == null)
				{
					throw new NullPointerException("Can not delegate the newly set box definitions to the client because universal client connector is null.");
				}
				else
				{
					getUniversalClientConnector().command_setBoxDefinitions(boxDefinitions);
				}
				break;
			case UNIVERSAL_CLIENT_CONNECTOR:
				universalClientConnector = (MainUIExtensionClientRpc) value;
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
	
	public static BoxInfoCollection getBoxDefinitions()
	{
		return boxDefinitions;
	}
	
	public static MainUIExtensionClientRpc getUniversalClientConnector()
	{
		return universalClientConnector;
	}

	public static boolean isApplicationReadyToServe()
	{
		return (getContext() != null) && 
				(getConfig() != null) && getConfig().isValid() && 
				(getJadeTopologies() != null) && (getJadeTopologies().getConnectedTopologies().size() > 1); 
	}
}
