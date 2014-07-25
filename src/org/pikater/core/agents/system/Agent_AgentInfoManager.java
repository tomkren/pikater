package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaAbstractCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing;
import org.pikater.core.agents.experiment.recommend.Agent_Recommender;
import org.pikater.core.agents.experiment.search.Agent_Search;
import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxProvider;
import org.pikater.core.agents.system.agentInfoManager.AgentInfoManagerCommunicator;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.subtrees.agent.NewAgent;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.SaveAgentInfo;
import org.pikater.core.ontology.subtrees.model.Models;
import org.reflections.Reflections;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPAService;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class Agent_AgentInfoManager extends PikaterAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6530381228391705988L;

	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();

		registerWithDF(AgentNames.AGENTINFO_MANAGER);

		for (Ontology ontologyI : getOntologies()) {
			getContentManager().registerOntology(ontologyI);
		}

		log("Agent " + getName() + " started");

		MessageTemplate newAgentInfoTemplate =
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		this.addBehaviour(new AchieveREResponder(this, newAgentInfoTemplate) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 324270572581200118L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request) {

				Action action = null;
				try {
					action = (Action) getContentManager().extractContent(
							request);
				} catch (UngroundedException e) {
				    logError(e.getMessage(), e);
				} catch (CodecException e) {
					logError(e.getMessage(), e);
				} catch (OntologyException e) {
					logError(e.getMessage(), e);
				}

				if (action.getAction() instanceof GetAgentInfos) {
					return respondToGetAgentInfos(request, action);
				}

				if (action.getAction() instanceof NewAgent) {
					return respondToNewAgent(request, action);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				logError("Failure responding to request: " + request.getContent());
				return failure;
			}

		});
		

		initializationAgentInfo();

	}

	private void initializationAgentInfo() {
		List<Class<? extends Agent_AbstractExperiment>> classes =
				getAllExperimmentAgentClasses();
		
		wakeUpAgentInfo(classes);
	}

	private void wakeUpAgentInfo(
			List<Class<? extends Agent_AbstractExperiment>> agentClasses) {
		
		for (Class<? extends Agent_AbstractExperiment> agentClassI : agentClasses) {

			createAgent(
					agentClassI.getName(), agentClassI.getName(), null);
		}
		
		for (Class<? extends Agent_AbstractExperiment> agentClassI : agentClasses) {
		
			AgentInfo agentInfo = getAgentInfo(this, agentClassI.getName());
			saveAgentInfo(agentInfo);
		}
		
		Thread shutDownAgents = new ShutDownAgents(this, agentClasses);
		shutDownAgents.start();
	}
	
	private List<Class<? extends Agent_AbstractExperiment>> getAllExperimmentAgentClasses() {
		
		List<Class<? extends Agent_AbstractExperiment>> allAgentClasses =
				new ArrayList<Class<? extends Agent_AbstractExperiment>>();

		allAgentClasses.addAll(
				getExperimmentAgentClasses(
						Agent_DataProcessing.class));
		allAgentClasses.remove(Agent_DataProcessing.class);

		allAgentClasses.addAll(
				getComputingAgentClasses(
						Agent_ComputingAgent.class));
		allAgentClasses.remove(Agent_ComputingAgent.class);
		allAgentClasses.remove(Agent_WekaAbstractCA.class);

		allAgentClasses.addAll(
				getExperimmentAgentClasses(
						Agent_Search.class));
		allAgentClasses.remove(Agent_Search.class);
	
		allAgentClasses.addAll(
				getExperimmentAgentClasses(
						Agent_Recommender.class));
		allAgentClasses.remove(Agent_Recommender.class);

		allAgentClasses.addAll(
				getExperimmentAgentClasses(
						Agent_VirtualBoxProvider.class));
		allAgentClasses.remove(Agent_VirtualBoxProvider.class);
		
		return allAgentClasses;
	}
	
	private List<Class<? extends Agent_AbstractExperiment>> getExperimmentAgentClasses(
			Class<? extends Agent_AbstractExperiment> agentClass) {
		
		String packageToSearch = agentClass.getPackage().getName();
		Reflections reflections = new Reflections(packageToSearch);

		Set<Class<? extends Agent_AbstractExperiment>> allClassesSet = 
		     reflections.getSubTypesOf(Agent_AbstractExperiment.class);
		 
		List<Class<? extends Agent_AbstractExperiment>> allClasses =
				new ArrayList<Class<? extends Agent_AbstractExperiment>>(allClassesSet);

		return allClasses;
	}
	
	private List<Class<? extends Agent_AbstractExperiment>> getComputingAgentClasses(
			Class<? extends Agent_AbstractExperiment> agentClass) {
		
		String packageToSearch = agentClass.getPackage().getName();
		Reflections reflections = new Reflections(packageToSearch);

		Set<Class<? extends Agent_ComputingAgent>> allClassesSet = 
		     reflections.getSubTypesOf(Agent_ComputingAgent.class);
		 
		List<Class<? extends Agent_AbstractExperiment>> allClasses =
				new ArrayList<Class<? extends Agent_AbstractExperiment>>(allClassesSet);

		return allClasses;
	}

	private ACLMessage respondToGetAgentInfos(ACLMessage request, Action action) {

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfoManagerCommunicator communicator =
				new AgentInfoManagerCommunicator(this);
		
		AgentInfos agenInfos = communicator.getAgentInfos();
		Models models = communicator.getAllModels();
		agenInfos.importModels(models);
		
		Result r = new Result(action, agenInfos);
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;

	}

	protected ACLMessage respondToNewAgent(ACLMessage request, Action action) {

		NewAgent newAgent = (NewAgent)action.getAction();
		String agentClassName = newAgent.getAgentClassName();
		
		Class<? extends Agent_AbstractExperiment> agentClass = null;
		try {
			agentClass = (Class<? extends Agent_AbstractExperiment>) Class.forName(agentClassName);
		} catch (ClassNotFoundException e) {
			logError(e.getMessage(), e);
		}
		
		List<Class<? extends Agent_AbstractExperiment>> classes =
				new ArrayList<Class<? extends Agent_AbstractExperiment>>();
		classes.add(agentClass);
		
		wakeUpAgentInfo(classes);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}

	
	private AgentInfo getAgentInfo(PikaterAgent agent, String agentName) {
		
		if (agent == null) {
			throw new IllegalArgumentException(
					"Argument agent can't be null");
		}
		if (agentName == null || agentName.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Argument agentName can't be null or empty");
		}


		AID agentRecieverAID = new AID(agentName, false);
		Ontology ontology = AgentInfoOntology.getInstance();

		ACLMessage getAgentInfomsg = new ACLMessage(ACLMessage.REQUEST);
		getAgentInfomsg.addReceiver(agentRecieverAID);
		getAgentInfomsg.setSender(agent.getAID());
		getAgentInfomsg.setLanguage(agent.getCodec().getName());
		getAgentInfomsg.setOntology(ontology.getName());

		GetAgentInfo getAgentInfo = new GetAgentInfo();
		
		Action action = new Action(agent.getAID(), getAgentInfo);
		
		try {
			agent.getContentManager().fillContent(getAgentInfomsg, action);
			ACLMessage agentInfoMsg = FIPAService
					.doFipaRequestClient(agent, getAgentInfomsg);

			Action replyAction = (Action) getContentManager()
					.extractContent(agentInfoMsg);
			
			AgentInfo agentInfo = (AgentInfo) replyAction.getAction();

			return agentInfo;
			
		} catch (FIPAException e) {
			agent.logError(e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}
		
		return null;
	}
	
	
	private void saveAgentInfo(AgentInfo agentInfo) {
		
		SaveAgentInfo saveAgentInfo = new SaveAgentInfo();
		saveAgentInfo.setAgentInfo(agentInfo);
		
		AID receiver = new AID(AgentNames.DATA_MANAGER, false);
		Ontology ontology = AgentInfoOntology.getInstance();
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(receiver);
		request.setLanguage(getCodec().getName());
		request.setOntology(ontology.getName());
	        
		try {
			getContentManager().fillContent(request,
					new Action(receiver, saveAgentInfo));
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}
		
		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(this, request, 10000);
		} catch (FIPAException e) {
		}

	}

}



class ShutDownAgents extends Thread {

	private List<Class<? extends Agent_AbstractExperiment>> agentClasses;
	private Agent_AgentInfoManager agent;

	public ShutDownAgents(Agent_AgentInfoManager agent,
			List<Class<? extends Agent_AbstractExperiment>> agentClasses) {
		this.agent = agent;
		this.agentClasses = agentClasses;
	}
	
    public void run() {
 	
		try {
			for (int i = 0; i < 30; i++) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			agent.logError(e.getMessage(), e);
		}
	
		for (Class<? extends Agent_AbstractExperiment> classI : agentClasses){
			agent.log("Agent " + classI.getName() + " was killed");
			agent.killAgent(classI.getName());
			
		}
    }

}