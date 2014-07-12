package org.pikater.core.agents.experiment.computing.computing;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
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
import java.util.Random;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.experiment.computing.Agent_ComputingAgent;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.data.GetData;
import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.SaveAgent;
import org.pikater.core.ontology.subtrees.result.PartialResults;
import org.pikater.core.ontology.subtrees.task.Task;

public class ComputingComminicator {

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
	
	public ACLMessage sendGetDataReq(Agent_ComputingAgent agent, String fileName) {

		AID[] ARFFReaders;
		AID reader = null;
		ACLMessage msgOut = null;
		// Make the list of reader agents
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentNames.ARRFF_READER);
		template.addServices(sd);
		try {
			GetData getData = new GetData();

			DFAgentDescription[] result = DFService.search(agent, template);

			ARFFReaders = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				if (agent.isSameNode(result[i].getName())) {
					// prefer local reader for O2A transfer
					reader = result[i].getName();
					agent.log("preferring reader " + reader.getName());
					getData.setO2aAgent(agent.getLocalName());
					break;
				}
				ARFFReaders[i] = result[i].getName();
			}

			// randomly choose one of the readers if none preferred
			if (reader == null) {
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(result.length);
				reader = ARFFReaders[randomInt];
			}

			agent.log("using reader " + reader + ", filename: " + fileName);

			Ontology ontology = DataOntology.getInstance();

			// request
			msgOut = new ACLMessage(ACLMessage.REQUEST);
			msgOut.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msgOut.setLanguage(agent.getCodec().getName());
			msgOut.setOntology(ontology.getName());
			msgOut.addReceiver(reader);
			msgOut.setConversationId("get-data_" + agent.convId++);
			// content
			getData.setFileName(fileName);
			Action a = new Action();
			a.setAction(getData);
			a.setActor(agent.getAID());
			agent.getContentManager().fillContent(msgOut, a);

		} catch (FIPAException fe) {
			agent.logError("", fe);
			return null;
		} catch (CodecException e) {
			agent.logError("", e);
			return null;
		} catch (OntologyException e) {
			agent.logError("", e);
			return null;
		}
		return msgOut;
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
			fe.printStackTrace();
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
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
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
				agent.logError("", ce);
			} catch (OntologyException oe) {
				agent.logError("", oe);
			}

		} catch (Exception e) {
			agent.logError("", e);
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
