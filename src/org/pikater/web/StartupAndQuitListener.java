package org.pikater.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.pikater.shared.logging.GeneralPikaterLogger;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.util.IOUtils;
import org.pikater.web.vaadin.MyResources;
import org.pikater.web.config.JadeTopologies;
import org.pikater.web.config.ServerConfiguration;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;

@WebListener
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
		
		// TODO: first check connection to database
		
		// this must be first
		ServerConfigurationInterface.setField(ServerConfItem.CONTEXT, event.getServletContext());
		IOUtils.setAbsoluteBaseAppPath(event.getServletContext().getRealPath("/"));
		
		// set the application-wide logger - this must be second
		PikaterLogger.setLogger(new GeneralPikaterLogger()
		{
			private final Logger innerLogger = Logger.getLogger("log4j");
			
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
		
		// TODO: delete this eventually (CustomConfiguredUI) handles it
		if(!PikaterJobScheduler.initStaticScheduler(IOUtils.getAbsolutePath(IOUtils.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class)))
		{
			throw new IllegalStateException("Application won't serve until the above errors are fixed.");
		}
		
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
	}
	
	/**
	 * Application shutdown	event.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		PikaterJobScheduler.shutdownStaticScheduler();
	}
}
