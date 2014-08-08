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

import org.pikater.core.AgentNames;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.result.PartialResults;
import org.pikater.core.ontology.subtrees.task.Task;

public class ComputingCommunicator {

	public ACLMessage executeTask(Agent_ComputingAgent agent, ACLMessage req) {
		
		ACLMessage resultMsg = req.createReply();
		if (agent.acceptTask()) {
			resultMsg.setPerformative(ACLMessage.AGREE);
			agent.taskFIFO.addLast(req);

			if (agent.taskFIFO.size() == 1) {
				if (!agent.executionBehaviour.isRunnable()) {
					agent.executionBehaviour.restart();
				}
			}

		} else {
			resultMsg.setPerformative(ACLMessage.REFUSE);
			resultMsg.setContent("(Computing agent overloaded)");
		}
		return resultMsg;
	}
	
	/*
	 * Send partial results to the GUI Agent(s) call it after training or during
	 * training?
	 */
	protected void sendResultsToGUI(Agent_ComputingAgent agent,
			Boolean first_time, Task _task, List _evaluations) {

		ACLMessage msgOut = new ACLMessage(ACLMessage.INFORM);
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNames.GUI_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] gui_agents = DFService.search(agent, template);
			for (int i = 0; i < gui_agents.length; ++i) {
				msgOut.addReceiver(gui_agents[i].getName());
			}
		} catch (FIPAException fe) {
			agent.logError(fe.getMessage(), fe);
		}

		msgOut.setConversationId("partial-results");

		PartialResults content = new PartialResults();
		content.setResults(_evaluations);

		if (first_time) {
			content.setTask(_task);
		}
		try {
			agent.getContentManager().fillContent(msgOut, content);
		} catch (CodecException e) {
			agent.logError(e.getMessage(), e);
		} catch (OntologyException e) {
			agent.logError(e.getMessage(), e);
		}

		agent.send(msgOut);
	}

	public ACLMessage sendOptions(Agent_ComputingAgent agent, ACLMessage request) {
		ACLMessage msgOut = request.createReply();
		msgOut.setPerformative(ACLMessage.INFORM);
		try {
			// Prepare the content
			ContentElement content = agent.getContentManager().extractContent(
					request); // TODO exception block?
			Result result = new Result((Action) content, agent.agentOptions);

			try {
				// Let JADE convert from Java objects to string
				agent.getContentManager().fillContent(msgOut, result);

			} catch (CodecException ce) {
				agent.logError(ce.getMessage(), ce);
			} catch (OntologyException oe) {
				agent.logError(oe.getMessage(), oe);
			}

		} catch (Exception e) {
			agent.logError(e.getMessage(), e);
		}

		return msgOut;

	}

	public String saveAgentToFile(Agent_ComputingAgent agent ) throws IOException, CodecException,
			OntologyException, FIPAException {

		Agent agentOnt = agent.currentTask.getAgent();
		agentOnt.setObject(agent.getAgentObject());
		
		
		SaveAgent saveAgent = new SaveAgent();

		saveAgent.setAgent(agentOnt);

		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID(AgentNames.MANAGER_AGENT, false));
		request.setOntology(TaskOntology.getInstance().getName());
		request.setLanguage(agent.getCodec().getName());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Action a = new Action();
		a.setActor(agent.getAID());
		a.setAction(saveAgent);

		agent.getContentManager().fillContent(request, a);
		ACLMessage reply = FIPAService.doFipaRequestClient(agent, request);

		String objectFilename = reply.getContent();

		return objectFilename;
	}
}
