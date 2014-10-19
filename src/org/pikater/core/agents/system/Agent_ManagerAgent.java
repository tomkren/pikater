package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.manager.ManagerAgentRequestResponder;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.configuration.Argument;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.subtrees.management.CreateAgent;
import org.pikater.core.ontology.subtrees.management.GetComputerInfo;
import org.pikater.core.ontology.subtrees.management.KillAgent;
import org.pikater.core.ontology.subtrees.management.LoadAgent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.ping.Ping;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * Agent which controls other agents
 * performs creating, killing, loading...
 *
 */
public class Agent_ManagerAgent extends PikaterAgent {

	private static final long serialVersionUID = 4898611781694963107L;

	private Map<String, AgentController> agentsNames =
			new HashMap<String, AgentController>();
	private ManagerAgentRequestResponder responder =
			new ManagerAgentRequestResponder(this);

	/**
	 * Get ontologies which is using this agent
	 */
	@Override
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();

		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(DataOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());

		return ontologies;
	}

	/**
	 * Agent setup
	 */
	@Override
	protected void setup() {
		initDefault();

		File data = new File(CoreConfiguration.getSavedResultsPath());
		if (!data.exists()) {
			logInfo("Creating directory saved");
			if (data.mkdirs()) {
				logInfo("Succesfully created directory saved");
			} else {
				logSevere("Error creating directory saved");
			}
		}

		registerWithDF();


		Ontology ontology = AgentManagementOntology.getInstance();
		MessageTemplate msgTemplate = MessageTemplate.and(
				MessageTemplate.MatchOntology(ontology.getName()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));


		addBehaviour(new AchieveREResponder(this, msgTemplate) {

			private static final long serialVersionUID = 7L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				try {
					Action action = (Action)
							getContentManager().extractContent(request);
					
					if (action.getAction() instanceof LoadAgent) {
						
						return responder.respondToLoadAgent(request);
					} else if (action.getAction() instanceof SaveAgent) {

						return responder.respondToSaveAgent(request);
					} else if (action.getAction() instanceof CreateAgent) {
						
						return responder.respondToCreateAgent(request);
					} else if (action.getAction() instanceof KillAgent) {
					
						return responder.respondToKillAgent(request);
					} else if (action.getAction() instanceof GetComputerInfo) {
						
						return responder.respondToGetComputerInfo(request);
					} else if (action.getAction() instanceof Ping) {
						
						return responder.respondToPing(request);
					}

				} catch (OntologyException e) {
					logException("Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logException("Codec problem: " + e.getMessage(), e);
				} catch (Exception e) {
					logException("Unexpected error occured:", e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logSevere("Failure responding to request: "
						+ request.getContent().substring(0, 2048));
				return failure;
			}
		});

	}

	/**
	 * Creates agent in this container
	 * 
	 * @param type - class of agent
	 * @param name - agent name
	 */
	public String createAgentInMyContainer(String type, String name,
			Arguments args) {
		// get a container controller for creating new agents
		PlatformController container = getContainerController();

		Argument[] args2 = new Argument[0];

		if (args != null) {
			args2 = new Argument[args.size()];
			int i = 0;
			for (Argument argI : args.getArguments()) {
				args2[i] = argI;
				i++;
			}
		}
		
		String generatedName = generateName(name == null ? type : name);

		try {
				try {
					doCreateAgent(generatedName, type, container, args2);
				} catch (StaleProxyException e) {
					// we may have the class in DB
					logInfo("agent creation failed, trying to get JAR for type " + type
							+ " from DB");
					DataManagerService.getExternalAgent(this, type);
					// DataManager will save the JAR to FS if it finds it so we can
					// retry
					doCreateAgent(generatedName, type, container, args2);
				}
			
		 } catch (ControllerException e) {
			 logException(e.getMessage(), e);
			 return null;
		 }
		
		// provide agent time to register with DF etc.
		doWait(300);
		return generatedName;
	}
	
	/**
	 * Does agent creating in a selected container
	 * 
	 * @param name - agent name
	 * @param type - class of agent
	 * @throws ControllerException
	 */
	private void doCreateAgent(String name, String type,
			PlatformController container,
			Argument[] args) throws ControllerException {
		
		AgentController agent = container.createNewAgent(name, type, args);
		agentsNames.put(name, agent);
		agent.start();
	}

	/**
	 * Generates a unused name for a new agent
	 * 
	 * @param name - root of the name
	 */
	public String generateName(String name) {
		String result = name;
		if (nodeName != null && !nodeName.isEmpty()) {
			result += "-" + nodeName;
		}
		PlatformController container = getContainerController();
		try {
			container.getAgent(result);
		} catch (ControllerException exc) {
			// agent with the same name does not exist, we are good
			return result;
		}

		for (Integer i = 0; i < 10000; i++) {
			String currentName = name + i.toString();
			try {
				container.getAgent(currentName);
			} catch (ControllerException exc) {
				// agent with the same name does not exist, we are good
				result = currentName;
				break;
			}
		}
		return result;
	}
}
