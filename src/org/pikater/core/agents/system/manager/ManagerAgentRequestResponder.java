package org.pikater.core.agents.system.manager;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;

import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.system.Agent_ManagerAgent;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TerminationOntology;
import org.pikater.core.ontology.subtrees.agent.TerminateSelf;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.management.CreateAgent;
import org.pikater.core.ontology.subtrees.management.KillAgent;
import org.pikater.core.ontology.subtrees.management.LoadAgent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.ping.PingReply;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;

/**
 * User: Kuba
 * Date: 7.11.13
 * Time: 15:51
 */
public class ManagerAgentRequestResponder {
	private Agent_ManagerAgent managerAgent;

	public ManagerAgentRequestResponder(Agent_ManagerAgent managerAgent) {
		this.managerAgent = managerAgent;
	}

	public Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
		Object object;

		object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();

		return object;
	}

	public ACLMessage respondToSaveAgent(ACLMessage request) throws OntologyException, Codec.CodecException, IOException, ClassNotFoundException {
		Action a = (Action) managerAgent.getContentManager().extractContent(request);
		SaveAgent sa = (SaveAgent) a.getAction();

		int userID = sa.getUserID();

		org.pikater.core.ontology.subtrees.management.Agent agent = sa.getAgent();

		String name = agent.getName(); // TODO - zajistit unikatni pro konkretniho uzivatele
		Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

		String filename = userID + "_" + name + "_" + currentTimestamp.toString().replace(":", "-").replace(" ", "_");

		// save serialized object to file
		byte[] object = sa.getAgent().getObject();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CoreConfiguration.getSavedResultsPath() + filename + ".model"));

		oos.writeObject(toObject(object));
		oos.flush();
		oos.close();
		managerAgent.logInfo("Agent " + name + " saved to file" + filename + ".model");

		ACLMessage reply = request.createReply();
		reply.setContent(filename);
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	public ACLMessage respondToCreateAgent(ACLMessage request) throws OntologyException, CodecException {

		Action a = (Action) managerAgent.getContentManager().extractContent(request);
		CreateAgent createAgent = (CreateAgent) a.getAction();

		String agentName = createAgent.getName();
		String agentType = createAgent.getType();
		Arguments arguments = createAgent.getArguments();

		String agentNameCreated = managerAgent.createAgentInMyContainer(agentType, agentName, arguments);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(agentNameCreated);
		managerAgent.logInfo("Agent " + agentNameCreated + " created.");

		return reply;
	}

	public ACLMessage respondToKillAgent(ACLMessage request) throws OntologyException, CodecException, FIPAException {
		Action a = (Action) managerAgent.getContentManager().extractContent(request);
		KillAgent killAgent = (KillAgent) a.getAction();

		String agentName = killAgent.getName();
		AID agentAID = new AID(agentName, false);

		managerAgent.logInfo("Request to kill " + agentName + " agent");
		Ontology ontology = TerminationOntology.getInstance();
		Codec codec = managerAgent.getCodec();

		ACLMessage msgToAgent = new ACLMessage(ACLMessage.REQUEST);
		msgToAgent.setLanguage(codec.getName());
		msgToAgent.setOntology(ontology.getName());
		msgToAgent.addReceiver(agentAID);

		Action actionToAgent = new Action(agentAID, new TerminateSelf());
		managerAgent.getContentManager().fillContent(msgToAgent, actionToAgent);

		ACLMessage replyFromAgent = FIPAService.doFipaRequestClient(managerAgent, msgToAgent, 10000);
		if (replyFromAgent == null) {
			throw new FailureException("Agent didn't terminate in within 10 sec: " + agentName);
		}
		managerAgent.logInfo("Terminated " + agentName);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}

	public ACLMessage respondToLoadAgent(ACLMessage request) throws OntologyException, Codec.CodecException, IOException, ClassNotFoundException, ControllerException {

		Action a = (Action) managerAgent.getContentManager().extractContent(request);
		LoadAgent la = (LoadAgent) a.getAction();
		ExecuteTask fa = la.getFirstAction();

		Agent newAgent;

		if (la.getObject() != null) {
			newAgent = (Agent) toObject(la.getObject());
		} else {

			// read agent from file
			String filename = CoreConfiguration.getSavedResultsPath() + la.getFilename() + ".model";

			//Construct the ObjectInputStream object
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));

			newAgent = (Agent) inputStream.readObject();

			inputStream.close();
		}

		managerAgent.logInfo("Resurrected agent : " + newAgent);
		// TODO kdyz se ozivuje 2x ten samej -> chyba

		String agentName = managerAgent.generateName(la.getFilename());

		if (newAgent != null) {
			// get a container controller for creating new agents

			ContainerController container = managerAgent.getContainerController();
			AgentController controller = container.acceptNewAgent(agentName, newAgent);
			controller.start();

		} else {
			throw new ControllerException("Agent not created.");
		}

		managerAgent.logInfo("Loaded agent:   " + agentName);

		ACLMessage reply;

		if (fa != null) {
			// send message with fa action to the loaded agent
			Action ac = new Action();
			ac.setAction(fa);
			ac.setActor(request.getSender());

			Ontology ontology = AgentManagementOntology.getInstance();

			ACLMessage first_message = new ACLMessage(ACLMessage.REQUEST);
			first_message.setLanguage(managerAgent.getCodec().getName());
			first_message.setOntology(ontology.getName());
			first_message.addReceiver(new AID(agentName, AID.ISLOCALNAME));
			first_message.clearAllReplyTo();
			first_message.addReplyTo(request.getSender());
			first_message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			first_message.setConversationId(request.getConversationId());

			managerAgent.getContentManager().fillContent(first_message, ac);
			managerAgent.send(first_message);
		}
		reply = request.createReply();
		reply.setContent(newAgent.getLocalName());
		reply.setPerformative(ACLMessage.INFORM);

		return reply;
	}

	public ACLMessage respondToGetComputerInfo(ACLMessage request) throws OntologyException, CodecException {

		Action a = (Action) managerAgent.getContentManager().extractContent(request);

		managerAgent.logInfo("Request to get ComputerInfo");
		Ontology ontology = AgentManagementOntology.getInstance();
		Codec codec = managerAgent.getCodec();

		int cores = Runtime.getRuntime().availableProcessors();
		String osName = System.getProperty("os.name");

		ComputerInfo computerInfo = new ComputerInfo();
		computerInfo.setOperationSystem(osName);
		computerInfo.setNumberOfCores(cores);

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ontology.getName());

		Result result = new Result(a.getAction(), computerInfo);

		try {
			managerAgent.getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			managerAgent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			managerAgent.logException(e.getMessage(), e);
		}

		return reply;
	}

	public ACLMessage respondToPing(ACLMessage request) throws OntologyException, CodecException {

		Action a = (Action) managerAgent.getContentManager().extractContent(request);

		managerAgent.logInfo("Request to get Ping");
		Ontology ontology = AgentManagementOntology.getInstance();
		Codec codec = managerAgent.getCodec();

		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setLanguage(codec.getName());
		reply.setOntology(ontology.getName());

		Result result = new Result(a.getAction(), new PingReply());

		try {
			managerAgent.getContentManager().fillContent(reply, result);
		} catch (CodecException e) {
			managerAgent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			managerAgent.logException(e.getMessage(), e);
		}

		return reply;

	}

}
