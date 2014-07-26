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

import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.managerAgent.ManagerAgentRequestResponder;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.configuration.Argument;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.subtrees.management.CreateAgent;
import org.pikater.core.ontology.subtrees.management.GetComputerInfo;
import org.pikater.core.ontology.subtrees.management.KillAgent;
import org.pikater.core.ontology.subtrees.management.LoadAgent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Agent_ManagerAgent extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4898611781694963107L;

	private Map<String, AgentController> agentsNames =
			new HashMap<String, AgentController>();
	private ManagerAgentRequestResponder responder =
			new ManagerAgentRequestResponder(this);

	public static String saveDirectoryPath = "core"
			+ System.getProperty("file.separator") + "saved"
			+ System.getProperty("file.separator");

	@Override
	public java.util.List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();

		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(DataOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();

		File data = new File(saveDirectoryPath);
		if (!data.exists()) {
			log("Creating directory saved");
			if (data.mkdirs()) {
				log("Succesfully created directory saved");
			} else {
				logError("Error creating directory saved");
			}
		}

		registerWithDF();


		Ontology ontology = AgentManagementOntology.getInstance();
		MessageTemplate mt = MessageTemplate.and(
				MessageTemplate.MatchOntology(ontology.getName()),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST));


		addBehaviour(new AchieveREResponder(this, mt) {

			private static final long serialVersionUID = 7L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(
							request);
					if (a.getAction() instanceof LoadAgent) {
						
						return responder.respondToLoadAgent(request);
					} else if (a.getAction() instanceof SaveAgent) {

						return responder.respondToSaveAgent(request);
					} else if (a.getAction() instanceof CreateAgent) {
						
						return responder.respondToCreateAgent(request);
					} else if (a.getAction() instanceof KillAgent) {
						
						return responder.respondToKillAgent(request);
					} else if (a.getAction() instanceof GetComputerInfo) {
					
						return responder.respondToGetComputerInfo(request);
					}

				} catch (OntologyException e) {
					e.printStackTrace();
					logError("Problem extracting content: " + e.getMessage());
				} catch (CodecException e) {
					e.printStackTrace();
					logError("Codec problem: " + e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: "
						+ request.getContent());
				return failure;
			}
		});

	}

	public String _createAgent(String type, String name, Arguments args) {
		// get a container controller for creating new agents
		PlatformController container = getContainerController();

		Argument[] args2 = new Argument[0];

		if (args != null) {
			args2 = new Argument[args.size()];
			int i = 0;
			for (Argument arg : args.getArguments()) {
				args2[i] = arg;
				i++;
			}
		}
		if (name == null){
			name = type;
		}
		String generatedName = generateName(name);

		try {
			try {
				doCreateAgent(generatedName, type, container, args2);
			} catch (StaleProxyException e) { // we may have the class in DB
				log("agent creation failed, trying to get JAR for type " + type + " from DB");
				DataManagerService.getExternalAgent(this, type);
				// DataManager will save the JAR to FS if it finds it so we can retry
				doCreateAgent(generatedName, type, container, args2);
			}
		} catch (ControllerException e) {
			e.printStackTrace();
			return null;
		}

		// provide agent time to register with DF etc.
		doWait(300);
		return generatedName;
	}
	
	private void doCreateAgent(String name, String type, PlatformController container, Argument[] args) throws ControllerException {
		AgentController agent = container.createNewAgent(name, type, args);
		agentsNames.put(name, agent);
		agent.start();
	}

	private String generateName(String name) {
		if (nodeName != null && !nodeName.isEmpty()) {
			name = name + "-" + nodeName;
		}
		PlatformController container = getContainerController();
		try {
			container.getAgent(name);
		} catch (ControllerException exc) {
			// agent with the same name does not exist, we are good
			return name;
		}

		for (Integer i = 0; i < 10000; i++) {
			String currentName = name + i.toString();
			try {
				// TODO: write without exceptions
				container.getAgent(currentName);
			} catch (ControllerException exc) {
				// agent with the same name does not exist, we are good
				name = currentName;
				break;
			}
		}
		return name;
	}
}