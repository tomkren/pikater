package org.pikater.core.agents.system.planner;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.configuration.Arguments;
import org.pikater.core.agents.system.managerAgent.ManagerAgentCommunicator;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.management.GetComputerInfo;
import org.pikater.core.ontology.subtrees.task.ExecuteTaksOnCPUCore;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;

public class PlannerCommunicator {

	private PikaterAgent agent;
	
	public PlannerCommunicator(PikaterAgent agent) {
		this.agent = agent;
	}
	
	private AID[] getAllAgents(String agentType) {

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType(agentType);
		template.addServices(serviceDescription);

		AID[] foundAgents = null;
		try {
			DFAgentDescription[] result = DFService.search(agent, template);
			foundAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				foundAgents[i] = result[i].getName();
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		return foundAgents;
	}

	public void sendExecuteTask(Task task, AID agentManagerAID) {

		ExecuteTaksOnCPUCore executeTask = new ExecuteTaksOnCPUCore();
		executeTask.setTask(task);
		
		String CAtype = task.getAgent().getType();

		ManagerAgentCommunicator comm = new ManagerAgentCommunicator(
				AgentNames.MANAGER_AGENT);

		agent.log("Sending request to create CA " + CAtype);
		AID caAID = comm.createAgent(agent, CAtype, CAtype,
				new Arguments());
		agent.log("CA " + CAtype + " created by " + agentManagerAID.getName());

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(caAID);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(TaskOntology.getInstance().getName());

		Action executeAction = new Action(agent.getAID(), executeTask);
		try {
			agent.getContentManager().fillContent(msg, executeAction);
		} catch (CodecException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		}

		agent.addBehaviour(new OneTaskManagementBehaviour(agent, msg));
		
		int f=0;
		if (10 > f) {
			return;
		}
		
		try {
			ACLMessage reply = FIPAService.doFipaRequestClient(agent, msg);
			String repliedText = reply.getContent();
			
			if (reply.getPerformative() == ACLMessage.INFORM) {
				agent.log(repliedText);
			} else {
				agent.logError(repliedText);
			}
		} catch (FIPAException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public ComputerInfo getComputerInfo(AID managerAgentAID) {
		
		GetComputerInfo getComputerInfo = new GetComputerInfo();

		agent.log("Sending request to getComputerInfo ");

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(managerAgentAID);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(AgentManagementOntology.getInstance().getName());

		Action executeAction = new Action(agent.getAID(), getComputerInfo);
		try {
			agent.getContentManager().fillContent(msg, executeAction);
		} catch (CodecException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		}

		ACLMessage reply = null;
		try {
			 reply = FIPAService.doFipaRequestClient(agent, msg);

		} catch (FIPAException e) {
			agent.logError(e.getMessage());
			return null;
		}
		
		try {
			Result result = (Result) agent.getContentManager().extractContent(reply);
			ComputerInfo computerInfo = (ComputerInfo) result.getValue();
			return computerInfo;
			
		} catch (UngroundedException e) {
			agent.log(e.getMessage());
			e.printStackTrace();
		} catch (CodecException e) {
			agent.log(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.log(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	//TODO: casem smazat
	public ACLMessage respondToExecuteTask(ACLMessage request, Action a) {

		ExecuteTask executeTask = (ExecuteTask) a.getAction();
		Task task = executeTask.getTask();
		String CAtype = task.getAgent().getType();

		AID[] foundAgents = getAllAgents(AgentNames.MANAGER_AGENT);
		// TODO choose a slave node
		ManagerAgentCommunicator comm = new ManagerAgentCommunicator(
				AgentNames.MANAGER_AGENT);

		agent.log("Sending request to create CA " + CAtype);
		AID ca = comm.createAgent(agent, CAtype, CAtype,
				new Arguments());
		agent.log("CA " + CAtype + " created");

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(ca);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(TaskOntology.getInstance().getName());

		Action executeAction = new Action(agent.getAID(), executeTask);
		try {
			agent.getContentManager().fillContent(msg, executeAction);
		} catch (CodecException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			agent.logError(e.getMessage());
			e.printStackTrace();
		}

		agent.addBehaviour(new OneTaskManagementBehaviour(agent, msg));
		
		ACLMessage requestAcceptedMsg = request.createReply();
		requestAcceptedMsg.setPerformative(ACLMessage.INFORM);
		agent.log("Request " + executeTask.getClass().getSimpleName() +
				" GraphId: " + executeTask.getTask().getGraphId() +
				" accepted");
		return requestAcceptedMsg;
	}
}
