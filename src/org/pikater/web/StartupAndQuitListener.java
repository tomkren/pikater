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
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.JadeTopologies;
import org.pikater.web.config.ServerConfiguration;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.sharedresources.ThemeResources;

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
		
		// this must be first
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
		
		try
		{
			/*
			 * GENERAL NOTE: don't alter the code order. There may be some dependencies.
			 */
			
			announceCheckOrAction("setting initial application state & essential variables");
			ServerConfigurationInterface.setField(ServerConfItem.CONTEXT, event.getServletContext());
			ServerConfigurationInterface.setField(ServerConfItem.JADE_TOPOLOGIES, new JadeTopologies());
			IOUtils.setAbsoluteBaseAppPath(event.getServletContext().getRealPath("/"));

			announceCheckOrAction("reading & parsing application configuration file");
			ServerConfigurationInterface.setField(ServerConfItem.CONFIG, new ServerConfiguration(ThemeResources.prop_appConf.getSourceFile()));
			if(!ServerConfigurationInterface.getConfig().isValid())
			{
				throw new IllegalStateException(String.format("Configuration file '%s' is not valid. Double check it.", 
						ThemeResources.prop_appConf.getSourceFile().getAbsolutePath()));
			}
			
			announceCheckOrAction("database connection");
			/*
			if(!ServerConfigurationInterface.avoidUsingDBForNow() && !PostgreLobAccess.isDatabaseConnected())
			{
				// TODO: doesn't work 
				throw new IllegalStateException("Could not connect to database.");
			}
			*/

			// TODO: move this to CustomConfiguredUI eventually (upon exiting the app-launch wizard)
			announceCheckOrAction("getting known agents from core & initializing task scheduler");
			boolean initHere = true;
			if(initHere)
			{
				// init scheduler
				PikaterJobScheduler.initStaticScheduler(IOUtils.getAbsolutePath(IOUtils.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class));

				// init agent info
				AgentInfoCollection agentInfoCollection = new AgentInfoCollection();
				if(!ServerConfigurationInterface.getConfig().useDummyBoxes)
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
						throw new IllegalStateException("Could not fetch list of known agents from core.", e);
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
			
			PikaterLogger.log(Level.INFO, "\n**********************************************************\n"
					+ "APPLICATION SETUP SUCCESSFULLY FINISHED\n"
					+ "**********************************************************");
		}
		catch(Throwable t)
		{
			PikaterLogger.log(Level.SEVERE, "APPLICATION LAUNCH REQUIREMENTS WERE NOT MET. ABORTED.");
			throw t; // will be printed just afterwards
		}
	}
	
	/**
	 * Application shutdown	event.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		PikaterJobScheduler.shutdownStaticScheduler();
	}
	
	//---------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void announceCheckOrAction(String message)
	{
		PikaterLogger.log(Level.INFO, "**********************************************************\n"
				+ "CHECKING REQUIREMENT / DOING ACTION: " + message);
	}
}
