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
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.subtrees.agent.AgentClass;
import org.pikater.core.ontology.subtrees.agent.NewAgent;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.ExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentInfo.GetYourAgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.GetAgentInfos;
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

				if (action.getAction() instanceof GetAgentInfo) {
					return respondToGetAgentInfo(request, action);
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
		List<AgentClass> agentClasses =
				getAllExperimmentAgentClasses();
		
		DataManagerService service = new DataManagerService();
		AgentInfos savedAgentInfos = service.getAgentInfos(this);
		
		List<AgentClass> notSavedAgentClasses =
				notSavedClasses(agentClasses, savedAgentInfos);
		
		wakeUpAgentInfo(notSavedAgentClasses);
		
		Thread shutDownAgents = new ShutDownAgents(this, notSavedAgentClasses);
		shutDownAgents.start();
	}

	private void wakeUpAgentInfo(
			List<AgentClass> agentClasses) {
		
		for (AgentClass agentClassI : agentClasses) {

			createAgent(
					agentClassI.getAgentClass(), agentClassI.getAgentClass(), null);
		}
		
		for (AgentClass agentClassI : agentClasses) {
			
			AgentInfo agentInfo = getAgentInfo(this, agentClassI.getAgentClass());
			
			DataManagerService service = new DataManagerService();
			service.saveAgentInfo(this, agentInfo);
		}
		
	}
	
	private List<AgentClass> getAllExperimmentAgentClasses() {
		
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
		
		List<AgentClass> agentClasses = new ArrayList<AgentClass>();
		for (Class<? extends Agent_AbstractExperiment> agentClassI : allAgentClasses) {
			agentClasses.add(new AgentClass(agentClassI.getName()));
		}
		
		
		DataManagerService service = new DataManagerService();
		ExternalAgentNames externalAgentNames =
				service.getExternalAgentNames(this);
		agentClasses.addAll(externalAgentNames.getAgentNames());

		return agentClasses;
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

	private List<AgentClass> notSavedClasses(
			List<AgentClass> agentClasses,
			AgentInfos savedAgentInfos) {
		
		List<AgentClass> notSavedAgents = new ArrayList<AgentClass>();
		
		for (AgentClass agentClassI : agentClasses) {
			
			if (! savedAgentInfos.contains(agentClassI)) {
				notSavedAgents.add(agentClassI);
			}
		}
		return notSavedAgents;
	}
	
	private ACLMessage respondToGetAgentInfo(ACLMessage request, Action action) {
		
		GetAgentInfo getAgentInfo = (GetAgentInfo)action.getAction();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfoManagerCommunicator communicator =
				new AgentInfoManagerCommunicator(this);
		
		AgentInfo agenInfo = communicator.getAgentInfo("TODO");
		//Models models = communicator.getAllModels();
		//agenInfos.importModels(models);
		
		Result r = new Result(action, agenInfo);
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}

		return reply;		
	}
	
	private ACLMessage respondToGetAgentInfos(ACLMessage request, Action action) {

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfoManagerCommunicator communicator =
				new AgentInfoManagerCommunicator(this);
		
		AgentInfos agenInfos = communicator.getAgentInfos();
		//Models models = communicator.getAllModels();
		//agenInfos.importModels(models);
		
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
		
		List<AgentClass> classes = new ArrayList<AgentClass>();
		classes.add(new AgentClass(agentClassName));
		
		wakeUpAgentInfo(classes);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}

	public AgentInfo getAgentInfo(PikaterAgent agent, String agentName) {
		
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

		GetYourAgentInfo getAgentInfo = new GetYourAgentInfo();
		
		Action action = new Action(agent.getAID(), getAgentInfo);
		
		try {
			agent.getContentManager().fillContent(getAgentInfomsg, action);
			ACLMessage agentInfoMsg = FIPAService
					.doFipaRequestClient(agent, getAgentInfomsg);

			Action replyAction = (Action) agent.getContentManager()
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


}



class ShutDownAgents extends Thread {

	private List<AgentClass> agentClasses;
	private Agent_AgentInfoManager agent;

	public ShutDownAgents(Agent_AgentInfoManager agent,
			List<AgentClass> agentClasses) {
		this.agent = agent;
		this.agentClasses = agentClasses;
	}
	
    public void run() {

		for (AgentClass classI : agentClasses){
			agent.log("Agent " + classI.getAgentClass() + " was killed");
			agent.killAgent(classI.getAgentClass());
			
		}
    }

}