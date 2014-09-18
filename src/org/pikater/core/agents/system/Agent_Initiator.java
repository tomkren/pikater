package org.pikater.core.agents.system;

import jade.content.onto.Ontology;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.configuration.AgentConfiguration;
import org.pikater.core.configuration.Argument;
import org.pikater.core.configuration.Configuration;
import org.pikater.core.configuration.XmlConfigurationProvider;
import org.pikater.core.ontology.AgentManagementOntology;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Agent_Initiator extends PikaterAgent {

	private static final long serialVersionUID = -3908734088006529947L;

    private String fileName = null;

	@Override
	public List<Ontology> getOntologies() {
		
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}
	
	@Override
	protected void setup() {
		initDefault();
        registerWithDF(CoreAgents.INITIATOR.getName());

		logInfo("Agent " + getName() + " configuration " + fileName);

		// read agents from configuration
		try {
			XmlConfigurationProvider configProvider = new XmlConfigurationProvider(fileName);

			Configuration configuration = configProvider.getConfiguration();
			List<AgentConfiguration> agentConfigurations = configuration.getAgentConfigurations();
			for (AgentConfiguration agentConfiguration : agentConfigurations) {
				// Preimplemented jade agents do not count with named arguments,
				// convert to string if necessary
				Object[] arguments = processArgs(agentConfiguration.getArguments().toArray());
				Boolean creationSuccessful = this.createAgent(agentConfiguration.getAgentType(), agentConfiguration.getAgentName(), arguments);
				if (!creationSuccessful) {
					logSevere("Creation of agent " + agentConfiguration.getAgentName() + " failed.");
				}
			}
		} catch (Exception e) {
			this.logException(e.getMessage(), e);
		}

		addBehaviour(new TickerBehaviour(this, 60000) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2962563585712447816L;

			Calendar cal;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			protected void onTick() {
				cal = Calendar.getInstance();
				logInfo("tick=" + getTickCount() + " time=" + sdf.format(cal.getTime()));
			}
		});
	}

	public Boolean createAgent(String type, String name, Object[] args) {
		// get a container controller for creating new agents
		PlatformController container = getContainerController();

		if (nodeName != null && !nodeName.isEmpty()) {
			name = name + "-" + nodeName;
		}

		try {
			AgentController agent = container.createNewAgent(name, type, args);
			agent.start();
			// provide agent time to register with DF etc.
			doWait(300);
		} catch (ControllerException e) {
			logException("Exception while adding agent", e);
			return false;
		}
		return true;
	}

	public Object[] processArgs(Object[] args) {
		Object[] toReturn = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			Argument arg = (Argument) args[i];
			if (arg.getSendOnlyValue()) {
				toReturn[i] = arg.getValue();
			} else {
				toReturn[i] = args[i];
			}
		}
		return toReturn;
	}

	@Override
	public void initDefault() {
		Object[] args = getArguments();
		if (args != null)
		{
			if (args.length > 0)
			{
				fileName = (String) args[0];
			} 
			if (args.length > 1)
			{
				nodeName = (String) args[1];
			}
		}
		if(fileName == null)
		{
			fileName = CoreConfiguration.getCoreMasterConfigurationFilepath();
		}

		initLogging();
	}

}