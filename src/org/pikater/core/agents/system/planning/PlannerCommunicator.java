package org.pikater.core.agents.system.planning;

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

import org.pikater.core.agents.system.Agent_Planner;
import org.pikater.core.agents.system.manager.ManagerAgentService;
import org.pikater.core.configuration.Arguments;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.management.GetComputerInfo;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.shared.logging.core.ConsoleLogger;

public class PlannerCommunicator {

	private Agent_Planner agent;

	public PlannerCommunicator(Agent_Planner agent) {
		this.agent = agent;
	}

	public AID[] getAllAgents(String agentType) {

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
			ConsoleLogger.logThrowable("Unexpected error occured:", e);
		}

		return foundAgents;
	}

	/** Request creation or loading of a CA */
	public AID prepareCA(Task task, AID agentManagerAID) {
		AID result;

		if (task.getAgent().getModel() != null) {
			result = ManagerAgentService.loadAgent(agent, task.getAgent()
					.getModel(), agentManagerAID);
			agent.logInfo("CA model " + task.getAgent().getModel()
					+ " resurrected");
		} else {
			String resultCAtype = task.getAgent().getType();
			agent.logInfo("Sending request to create CA " + resultCAtype);
			result = ManagerAgentService.createAgent(agent, resultCAtype,
					resultCAtype, new Arguments(), agentManagerAID);
			// agent.log("CA " + CAtype + " created by " +
			// agentManagerAID.getName());
			agent.logInfo("CA " + resultCAtype + " created");
		}
		return result;
	}

	public void sendExecuteTask(Task task, AID agentManagerAID) {

		ExecuteTask executeTask = new ExecuteTask();
		executeTask.setTask(task);

		AID caAID = prepareCA(task, agentManagerAID);

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(caAID);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(TaskOntology.getInstance().getName());

		Action executeAction = new Action(agent.getAID(), executeTask);
		try {
			agent.getContentManager().fillContent(msg, executeAction);
		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}

		agent.addBehaviour(new OneTaskComputingCABehaviour(agent, msg));
	}

	public ComputerInfo getComputerInfo(AID managerAgentAID) {

		GetComputerInfo getComputerInfo = new GetComputerInfo();

		agent.logInfo("Sending request to getComputerInfo ");

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(managerAgentAID);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(agent.getCodec().getName());
		msg.setOntology(AgentManagementOntology.getInstance().getName());

		Action executeAction = new Action(agent.getAID(), getComputerInfo);
		try {
			agent.getContentManager().fillContent(msg, executeAction);
		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}

		ACLMessage reply = null;
		try {
			reply = FIPAService.doFipaRequestClient(agent, msg);

		} catch (FIPAException e) {
			agent.logSevere(e.getMessage());
			return null;
		}

		try {
			Result result = (Result) agent.getContentManager().extractContent(
					reply);
			return (ComputerInfo) result.getValue();

		} catch (UngroundedException e) {
			agent.logException(e.getMessage(), e);
		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}

		return null;
	}

}
