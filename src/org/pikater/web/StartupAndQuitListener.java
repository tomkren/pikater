package org.pikater.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.logging.GeneralPikaterLogger;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.util.IOUtils;
import org.pikater.web.sharedresources.ThemeResources;
import org.pikater.web.config.AgentInfoCollection;
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
		
		// TODO: move this to CustomConfiguredUI eventually (upon exiting the app-launch wizard)
		boolean initHere = true;
		boolean coreAgentInfo = true;
		if(initHere)
		{
			// init scheduler
			if(!PikaterJobScheduler.initStaticScheduler(IOUtils.getAbsolutePath(IOUtils.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class)))
			{
				throw new IllegalStateException("Application won't serve until the above errors are fixed.");
			}
			
			// init agent info
			AgentInfoCollection agentInfoCollection = new AgentInfoCollection();
			if(coreAgentInfo)
			{
				try
				{
					AgentInfos infos = WebToCoreEntryPoint.getAgentInfos();
					for(AgentInfo info : infos.getAgentInfos())
					{
						agentInfoCollection.addDefinition(info);
					}
				}
				catch (Exception e)
				{
					PikaterLogger.logThrowable("Can not fetch agent info collection from pikater core.", e);
				}
			}
			else
			{
				for(BoxType type : BoxType.values())
				{
					AgentInfo agentInfo = new AgentInfo();
					agentInfo.setOntologyClassName(type.toOntologyClass().getName());
					agentInfo.setAgentClassName(this.getClass().getName());
					agentInfo.setDescription(String.format("Some kind of a '%s' box.", type.name()));
					
					String name = null;
					switch(type)
					{
						case CHOOSE:
							name = "Klobása";
							break;
						case COMPUTE:
							name = "Vepřová kýta";
							break;
						case PROCESS_DATA:
							name = "Chleba";
							break;
						case OPTION:
							name = "Bobkový list";
							break;
						case INPUT:
							name = "Brambory";
							break;
						case MISC:
							name = "Pepř";
							break;
						case OUTPUT:
							name = "Sůl";
							break;
						case SEARCH:
							name = "Cibule";
							break;
						case COMPOSITE:
							name = "Protlak";
							break;
						default:
							break;
					}
					agentInfo.setName(name);
					
					NewOptions options = new NewOptions();
					options.addOption(new NewOption("IntRange", new IntegerValue(5), new RangeRestriction(new IntegerValue(2), new IntegerValue(10))));
					options.addOption(new NewOption("IntSet", new IntegerValue(5), new SetRestriction(false, new ArrayList<IValueData>(Arrays.asList(
							new IntegerValue(2),
							new IntegerValue(3),
							new IntegerValue(5),
							new IntegerValue(10))))
					));
					options.addOption(new NewOption("Double", new DoubleValue(1)));
					options.addOption(new NewOption("Boolean", new BooleanValue(true)));
					options.addOption(new NewOption("Float", new FloatValue(1)));
					options.addOption(new NewOption("QuestionMarkRange", new QuestionMarkRange(
							new IntegerValue(5), new IntegerValue(10), 3)));
					options.addOption(new NewOption("QuestionMarkSet", new QuestionMarkSet(new ArrayList<IValueData>(Arrays.asList(
							new IntegerValue(5), new IntegerValue(10))), 3)));
					
					agentInfo.setOptions(options);
					agentInfoCollection.addDefinition(agentInfo);
				}
			}
			ServerConfigurationInterface.setField(ServerConfItem.BOX_DEFINITIONS, agentInfoCollection);
		}
		
		// set parsed topologies to the application wide variable
		ServerConfigurationInterface.setField(ServerConfItem.JADE_TOPOLOGIES, new JadeTopologies());
		
		// *****************************************************************************************************
		PikaterLogger.log(Level.INFO, "***** Reading basic application configuration files. *****");
		
		ServerConfigurationInterface.setField(ServerConfItem.CONFIG, new ServerConfiguration(ThemeResources.prop_appConf.getSourceFile()));
		if(!ServerConfigurationInterface.getConfig().isValid())
		{
			PikaterLogger.log(Level.SEVERE, "Errors were encountered while parsing the application configuration. All client "
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
