package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.experiment.Agent_AbstractExperiment;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.agents.experiment.computing.Agent_WekaAbstractCA;
import org.pikater.core.agents.experiment.dataprocessing.Agent_DataProcessing;
import org.pikater.core.agents.experiment.recommend.Agent_Recommender;
import org.pikater.core.agents.experiment.search.Agent_Search;
import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxProvider;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.AgentInfoOntology;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.subtrees.agent.AgentClass;
import org.pikater.core.ontology.subtrees.agent.NewAgent;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentinfo.ExternalAgentNames;
import org.pikater.core.ontology.subtrees.agentinfo.GetAgentInfo;
import org.pikater.core.ontology.subtrees.agentinfo.GetAgentInfoVisibleForUser;
import org.pikater.core.ontology.subtrees.agentinfo.GetAgentInfos;
import org.pikater.core.ontology.subtrees.agentinfo.GetYourAgentInfo;
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

/**
 * 
 * This agent is manager of informations {@link AgentInfo} about
 * all experiment agents
 *
 */
public class Agent_AgentInfoManager extends PikaterAgent {

	private static final long serialVersionUID = 6530381228391705988L;
	
	/**
	 * Get {@link List<Ontology>} that utilizes this agent
	 * 
	 * @param agentClasses list of {@link AgentClass}
	 *        represents {@link PikaterAgent} to wake
	 */
	public List<Ontology> getOntologies() {

		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(AgentInfoOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());

		return ontologies;
	}

	@Override
	protected void setup() {
		initDefault();

		registerWithDF(CoreAgents.AGENTINFO_MANAGER.getName());

		for (Ontology ontologyI : getOntologies()) {
			getContentManager().registerOntology(ontologyI);
		}

		logInfo("Agent " + getName() + " started");
		
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
				    logException(e.getMessage(), e);
				    return null;
				} catch (CodecException e) {
					logException(e.getMessage(), e);
					return null;
				} catch (OntologyException e) {
					logException(e.getMessage(), e);
					return null;
				}

				if (action.getAction() instanceof GetAgentInfo) {
					return respondToGetAgentInfo(request, action);
				}
				
				if (action.getAction() instanceof GetAgentInfos) {
					return respondToGetAgentInfos(request, action);
				}

				if (action.getAction() instanceof GetAgentInfoVisibleForUser) {
					return respondToGetAgentInfoVisibleForUser(request, action);
				}
				
				if (action.getAction() instanceof NewAgent) {
					return respondToNewAgent(request, action);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);

				logSevere("Failure responding to request: " + request.getContent());
				return failure;
			}

		});
		

		//initializationAgentInfo();

	}

	/**
	 * Ensures {@link AgentInfo} structures initialization,
	 * obtaining information about agents
	 * 
	private void initializationAgentInfo() {
		List<AgentClass> agentClasses =
				getAllExperimmentAgentClasses();
		
		AgentInfos savedAgentInfos = DataManagerService.getAllAgentInfos(this);
		
		List<AgentClass> notSavedAgentClasses =
				notSavedClasses(agentClasses, savedAgentInfos);
		
		wakeUpAgentInfo(notSavedAgentClasses);
		
		Thread shutDownAgents = new ShutDownAgents(this, notSavedAgentClasses);
		shutDownAgents.start();
	}
	 */

	/**
	 * Wake agents, ask for {@link AgentInfo} structures,
	 * store structures by using {@link Agent_DataManager}
	 * 
	 * @param agentClasses list of {@link AgentClass}
	 *        represents {@link PikaterAgent} to wake
	 */
	private void wakeUpAgentInfo(
			List<AgentClass> agentClasses) {
		
		for (AgentClass agentClassI : agentClasses) {

			createAgent(
					agentClassI.getAgentClass(), agentClassI.getAgentClass(), null);
		}
		
		for (AgentClass agentClassI : agentClasses) {
			
			AgentInfo agentInfo = getAgentInfo(this, agentClassI.getAgentClass());
			
			DataManagerService.saveAgentInfo(this, agentInfo);
		}
		
	}

	/**
	 * Get Experiment all {@link PikaterAgent} classes in the system
	 * 
	 * @return {@link List<AgentClass>} classes of {@link PikaterAgent}
	 * 
	 */
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
			AgentClass agentClassOnt = new AgentClass(
					agentClassI.getName());
			agentClasses.add(agentClassOnt);
		}
		
		ExternalAgentNames externalAgentNames =
				DataManagerService.getExternalAgentNames(this);
		agentClasses.addAll(externalAgentNames.getAgentNames());

		return agentClasses;
	}
	
	private List<Class<? extends Agent_AbstractExperiment>> getExperimmentAgentClasses(
			Class<? extends Agent_AbstractExperiment> agentClass) {
		
		String packageToSearch = agentClass.getPackage().getName();
		Reflections reflections = new Reflections(packageToSearch);

		Set<Class<? extends Agent_AbstractExperiment>> allClassesSet = 
		     reflections.getSubTypesOf(Agent_AbstractExperiment.class);
		 
		return new ArrayList<Class<? extends Agent_AbstractExperiment>>(allClassesSet);
	}

	private List<Class<? extends Agent_AbstractExperiment>> getComputingAgentClasses(
			Class<? extends Agent_AbstractExperiment> agentClass) {
		
		String packageToSearch = agentClass.getPackage().getName();
		Reflections reflections = new Reflections(packageToSearch);

		Set<Class<? extends Agent_ComputingAgent>> allClassesSet = 
		     reflections.getSubTypesOf(Agent_ComputingAgent.class);
		 
		return new ArrayList<Class<? extends Agent_AbstractExperiment>>(allClassesSet);
	}

	/**
	 * Filter not saved classes
	 * 
	 * @param agentClasses {@link List<AgentClass>} - name of agent class
	 * @param savedAgentInfos {@link AgentInfos}
	 * @return {@link List<AgentClass>} new AgentInfos
	 * 
	 */
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
	
	/**
	 * Request for {@link AgentInfo}
	 * 
	 * @param request {@link ACLMessage} - message
	 * @param action {@link Action} - action {@link  GetAgentInfo}
	 * @return {@link ACLMessage} contains AgentInfo,
	 *         informations about agent
	 * 
	 */
	private ACLMessage respondToGetAgentInfo(ACLMessage request, Action action) {
		
		GetAgentInfo getAgentInfo = (GetAgentInfo)action.getAction();
		String agentClassName = getAgentInfo.getAgentClassName();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		
		AgentInfo agenInfo = DataManagerService
				.getAgentInfo(this, agentClassName);
		 
		//Models models = service.getAllModels();
		//agenInfos.importModels(models);
		
		Result result = new Result(action, agenInfo);
		try {
			getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;		
	}
	
	/**
	 * Request for all {@link AgentInfos}
	 * 
	 * @param request {@link ACLMessage} - message
	 * @param action {@link Action} - action {@link  GetAgentInfos}
	 * @return {@link ACLMessage} contains AgentInfos,
	 *         informations about agent
	 * 
	 */
	private ACLMessage respondToGetAgentInfos(ACLMessage request, Action action) {

		GetAgentInfos getAgentInfos = (GetAgentInfos)action.getAction();
		int userID = getAgentInfos.getUserID();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfos agenInfos = DataManagerService.getAgentInfos(this, userID);
		
		//Models models = communicator.getAllModels();
		//agenInfos.importModels(models);
		
		Result r = new Result(action, agenInfos);
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;

	}
	
	/**
	 * Request for {@link AgentInfos} (visible {@link AgentInfo})
	 * for actual user
	 * 
	 * @param request {@link ACLMessage} - message
	 * @param action {@link Action} - action {@link  GetAgentInfoVisibleForUser}
	 * @return {@link ACLMessage} contains AgentInfos,
	 *         informations about agent
	 * 
	 */
	private ACLMessage respondToGetAgentInfoVisibleForUser(ACLMessage request, Action action) {

		GetAgentInfoVisibleForUser getAgentInfoVisibleForUser =
				(GetAgentInfoVisibleForUser)action.getAction();
		int userID = getAgentInfoVisibleForUser.getUserID();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);

		AgentInfos agenInfosUser = DataManagerService.getAgentInfos(this, userID);
		AgentInfos agenInfosEveryone = DataManagerService.getAgentInfos(this, -1);
		
		AgentInfos agentInfos = new AgentInfos();
		agentInfos.addAgentInfo(agenInfosUser.getAgentInfos());
		agentInfos.addAgentInfo(agenInfosEveryone.getAgentInfos());
		
		//Models models = communicator.getAllModels();
		//agenInfos.importModels(models);
		
		Result r = new Result(action, agentInfos);
		try {
			getContentManager().fillContent(reply, r);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}

		return reply;

	}
	
	/**
	 * New {@link PikaterAgent} was added to system
	 * 
	 * @param agent {@link PikaterAgent} which sends request
	 * @param agentName - name of {@link PikaterAgent} which receive request
	 * @return {@link ACLMessage} - OK information message
	 * 
	 */
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

	/**
	 * Get {@link AgentInfo} from {@link PikaterAgent}
	 * 
	 * @param agent {@link PikaterAgent} which sends request
	 * @param agentName - name of {@link PikaterAgent} which receive request
	 * @return {@link AgentInfo} - informations about agent
	 * 
	 */
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
			
			return (AgentInfo) replyAction.getAction();
			
		} catch (FIPAException e) {
			agent.logException(e.getMessage(), e);
		} catch (Codec.CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}
		
		return null;
	}


}


/**
 * 
 * Thread for killing awakened agents
 *
 */
class ShutDownAgents extends Thread {

	private List<AgentClass> agentClasses;
	private Agent_AgentInfoManager agent;

	public ShutDownAgents(Agent_AgentInfoManager agent,
			List<AgentClass> agentClasses) {
		this.agent = agent;
		this.agentClasses = agentClasses;
	}
	
    public void run() {

		for (AgentClass classI : agentClasses) {
			agent.logInfo("Agent " +
					classI.getAgentClass() +
					" was killed");
			agent.killAgent(classI.getAgentClass());
			
		}
    }

}