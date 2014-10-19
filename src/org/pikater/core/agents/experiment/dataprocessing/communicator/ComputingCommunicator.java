package org.pikater.core.agents.experiment.dataprocessing.communicator;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
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
import jade.util.leap.List;

import java.io.IOException;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.ontology.DurationOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.duration.Duration;
import org.pikater.core.ontology.subtrees.duration.SetDuration;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.result.PartialResults;
import org.pikater.core.ontology.subtrees.task.Task;

/**
 * 
 * Computing agent communicator
 *
 */
public abstract class ComputingCommunicator {

	/**
	 * Handles the incoming Task
	 * 
	 */
	public static ACLMessage handleTask(Agent_ComputingAgent agent,
			ACLMessage requestMsg) {
		
		ACLMessage resultMsg = requestMsg.createReply();
		if (agent.acceptTask()) {
			resultMsg.setPerformative(ACLMessage.AGREE);
			agent.taskFIFO.addLast(requestMsg);

			if ((agent.taskFIFO.size() == 1) &&
				(!agent.executionBehaviour.isRunnable())) {
				
				agent.executionBehaviour.restart();
			}

		} else {
			resultMsg.setPerformative(ACLMessage.REFUSE);
			resultMsg.setContent("(Computing agent overloaded)");
		}
		return resultMsg;
	}
	
	/**
	 * Executes a Duration Task
	 * 
	 */
	public static ACLMessage executeDurationTask(Agent_ComputingAgent agent,
			ACLMessage requestMsq) {
		
		ACLMessage resultMsg = requestMsq.createReply();
		if (agent.acceptTask()) {
			resultMsg.setPerformative(ACLMessage.PROPOSE);
			agent.taskFIFO.addFirst(requestMsq);

			if ((agent.taskFIFO.size() == 1) &&
				(!agent.executionBehaviour.isRunnable())) {
				
				agent.executionBehaviour.restart();
			}

		} else {
			resultMsg.setPerformative(ACLMessage.REFUSE);
			resultMsg.setContent("(Computing agent overloaded)");
		}
		return resultMsg;
	}
	
	/**
	 * Sends a last duration
	 * 
	 */
	public static void sendLastDuration(Agent_ComputingAgent agent) {
		
		Duration duration = new Duration();
		duration.setStart(agent.getLastStartDate());
		duration.setDurationMiliseconds(agent.getLastDuration());
		
		SetDuration setDuration = new SetDuration();
		setDuration.setDuration(duration);
		
		AID aidDuration = new AID(CoreAgents.DURATION.getName(), false);
		
		ACLMessage durationMessage = new ACLMessage(ACLMessage.REQUEST);
		durationMessage.setOntology(DurationOntology.getInstance().getName());
		durationMessage.addReceiver(aidDuration);
		durationMessage.setLanguage(agent.getCodec().getName());
		
		Action action = new Action();
		action.setAction(setDuration);
		action.setActor(agent.getAID());
		
		try {
			agent.getContentManager().fillContent(durationMessage, action);
		} catch (CodecException | OntologyException e) {
			agent.logException("Codec/Ontology exception in "
					+ "Computing Communicator", e);
		}
		agent.send(durationMessage);
	}
	
	/**
	 * Send partial results to the GUI Agent(s) call it after training or during
	 * training?
	 */
	public static void sendResultsToGUI(Agent_ComputingAgent agent,
			Boolean firstTime, Task task, List evaluations) {

		DFAgentDescription template = new DFAgentDescription();
		
		ServiceDescription serviceDuration = new ServiceDescription();
		serviceDuration.setType(CoreAgents.GUI_AGENT.getName());
		template.addServices(serviceDuration);
		
		ACLMessage msgOut = new ACLMessage(ACLMessage.INFORM);
		
		try {
			DFAgentDescription[] guiAgents = DFService.search(agent, template);
			for (int i = 0; i < guiAgents.length; ++i) {
				msgOut.addReceiver(guiAgents[i].getName());
			}
		} catch (FIPAException fe) {
			agent.logException(fe.getMessage(), fe);
		}

		msgOut.setConversationId("partial-results");

		PartialResults content = new PartialResults();
		content.setResults(evaluations);

		if (firstTime) {
			content.setTask(task);
		}
		try {
			agent.getContentManager().fillContent(msgOut, content);
		} catch (CodecException e) {
			agent.logException(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logException(e.getMessage(), e);
		}

		agent.send(msgOut);
	}

	/**
	 * Sends options of the agent
	 * 
	 */
	public static ACLMessage sendOptions(Agent_ComputingAgent agent, ACLMessage request) {
		ACLMessage msgOut = request.createReply();
		msgOut.setPerformative(ACLMessage.INFORM);
		try {
			// Prepare the content
			ContentElement content = agent.getContentManager().extractContent(
					request);
			Result result = new Result((Action) content, agent.agentOptions);

			// Let JADE convert from Java objects to string
			agent.getContentManager().fillContent(msgOut, result);
		} catch (CodecException ce) {
			agent.logException(ce.getMessage(), ce);
		} catch (OntologyException oe) {
			agent.logException(oe.getMessage(), oe);
		} catch (Exception e) {
			agent.logException("Unexpected error message:", e);
		}

		return msgOut;

	}

	/**
	 * Save this agent to file
	 * 
	 * @throws IOException
	 * @throws CodecException
	 * @throws OntologyException
	 * @throws FIPAException
	 */
	public static String saveAgentToFile(Agent_ComputingAgent agent
			) throws IOException, CodecException, OntologyException,
			FIPAException {

		Agent agentOnt = agent.currentTask.getAgent();
		agentOnt.setObject(agent.getAgentObject());
		
		
		SaveAgent saveAgent = new SaveAgent();
		saveAgent.setAgent(agentOnt);

		AID managerAID = new AID(CoreAgents.MANAGER_AGENT.getName(), false);
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(managerAID);
		request.setOntology(TaskOntology.getInstance().getName());
		request.setLanguage(agent.getCodec().getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action action = new Action();
		action.setActor(agent.getAID());
		action.setAction(saveAgent);

		agent.getContentManager().fillContent(request, action);
		ACLMessage reply = FIPAService.doFipaRequestClient(agent, request);

		return reply.getContent();
	}
}
