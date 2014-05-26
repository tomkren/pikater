package org.pikater.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.pikater.shared.AppHelper;
import org.pikater.shared.logging.GeneralPikaterLogger;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
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
		
		/*
		 * GENERAL NOTE: don't alter the code order. There may be some dependencies.
		 */
		
		// this must be first
		ServerConfigurationInterface.setField(ServerConfItem.CONTEXT, event.getServletContext());
		AppHelper.setAbsoluteBaseAppPath(event.getServletContext().getRealPath("/"));
		
		// set the application-wide logger - this must be second
		PikaterLogger.setLogger(new GeneralPikaterLogger()
		{
			private final Logger innerLogger = Logger.getAnonymousLogger(); 
			
			@Override
			public void logThrowable(String problemDescription, Throwable t)
			{
				innerLogger.log(Level.SEVERE, "exception occured: " + problemDescription + "\n" + throwableToStackTrace(t));
			}
			
			@Override
			public void log(Level logLevel, String message)
			{
				innerLogger.log(logLevel, message);
			}
		});
		
		// set parsed topologies to the application wide variable
		ServerConfigurationInterface.setField(ServerConfItem.JADE_TOPOLOGIES, new JadeTopologies());
		
		// *****************************************************************************************************
		PikaterLogger.log(Level.INFO, "***** Reading basic application configuration files. *****");
		
		ServerConfigurationInterface.setField(ServerConfItem.CONFIG, new ServerConfiguration(MyResources.prop_appConf.getSourceFile()));
		if(!ServerConfigurationInterface.getConfig().isValid())
		{
			PikaterLogger.log(Level.SEVERE, "Erros were encountered while parsing the application configuratio. All client "
					+ "requests will result in 'Service temporarily unavailable'.");
		}
		else
	    {
			PikaterLogger.log(Level.INFO, "Application settings were successfully read and parsed.");
	    }
		
		PikaterLogger.log(Level.INFO, "***** Finished reading .properties files. *****");
		// *****************************************************************************************************
		
		// initialize and start the cron job scheduler
		if(!PikaterJobScheduler.init(AppHelper.getAbsolutePath(AppHelper.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class)))
		{
			throw new IllegalStateException("Application won't serve until the above errors are fixed.");
		}
	}
	
	/**
	 * Application shutdown	event.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		PikaterJobScheduler.shutdown();
	}
}
