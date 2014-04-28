package org.pikater.web;

import java.util.logging.Level;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.pikater.web.vaadin.MyResources;
import org.pikater.web.config.JadeTopologies;
import org.pikater.web.config.ServerConfiguration;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;

public class StartupAndQuitListener implements ServletContextListener
{
	/**
	 * Application startup event.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		/*
	    // first let's see whether our app is clustered and replicates the servlet context
	    // event.getServletContext().setAttribute("config", this);
	 	ServletContext ctx = event.getServletContext();
	 	if(ctx.getAttribute("storage") == null) // servlet context is replicated and was successfully loaded
	 	{
	 		// create new 
	 		ctx.setAttribute("storage", SharedStorage.getInstance());
	 	}
	 	else
	 	{
	 		System.out.println("storage was already set");
	 	}
	 	*/
		
		// initialize the server data store - this is an absolute prerequisite
		ServerConfigurationInterface.setField(ServerConfItem.CONTEXT, event.getServletContext());
		
		// set parsed topologies to the application wide variable
		ServerConfigurationInterface.setField(ServerConfItem.JADE_TOPOLOGIES, new JadeTopologies());
		
		// *****************************************************************************************************
		AppLogger.log(Level.INFO, "***** Reading basic application configuration files. *****");
		
		ServerConfigurationInterface.setField(ServerConfItem.CONFIG, new ServerConfiguration(MyResources.prop_appConf));
		if(!ServerConfigurationInterface.getConfig().isValid())
		{
			AppLogger.log(Level.SEVERE, "Erros were encountered while parsing the application configuratio. All client "
					+ "requests will result in 'Service temporarily unavailable'.");
		}
		else
	    {
	    	AppLogger.log(Level.INFO, "Application settings were successfully read and parsed.");
	    }
		
		AppLogger.log(Level.INFO, "***** Finished reading .properties files. *****");
		// *****************************************************************************************************
	}
	
	/**
	 * Application shutdown	event.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		// no need to do anything
	}
}
