package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.planner.CPUCore;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.agents.system.planner.TaskToSolve;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;


public class Agent_Planner extends PikaterAgent {
	
	private static final long serialVersionUID = 820846175393846627L;

	private LinkedList<TaskToSolve> waitingToStartComputingTasks =
			new LinkedList<TaskToSolve>();
	private LinkedList<TaskToSolve> computingTasks =
			new LinkedList<TaskToSolve>();
	private Map <CPUCore, TaskToSolve> computingCores =
			new HashMap<CPUCore, TaskToSolve>();
	private List<CPUCore> untappedCores =
			new ArrayList<CPUCore>();
	
	private Map <AID, ComputerInfo> computerOwnedByManagerAgent =
			new HashMap<AID, ComputerInfo>();

	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}


	private void initCPUCores() {

		 DFAgentDescription template = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType(Agent_ManagerAgent.class.getName());
		 template.addServices(sd);
		 
		 DFAgentDescription[] result = null;
		 try {
			result = DFService.search(this, template);
		} catch (FIPAException e1) {
			logError(e1.getMessage(), e1);
		}
		if (result.length == 0) {
			return;
		}
	
		AID managerAgentAID = new AID(AgentNames.MANAGER_AGENT, false);
		
		PlannerCommunicator comminicator = new PlannerCommunicator(this);
		ComputerInfo computerInfo =
				comminicator.getComputerInfo(managerAgentAID);
		int numberOfCores = computerInfo.getNumberOfCores();
		
		computerOwnedByManagerAgent.put(managerAgentAID, computerInfo);
		for (int i = 0; i < numberOfCores; i++) {
			
			CPUCore cpuCore = new CPUCore(managerAgentAID, i);
			untappedCores.add(cpuCore);
		}
	}
	
	protected void setup() {
		initDefault();

		registerWithDF(AgentNames.PLANNER);

		// waiting to start ManagerAgent
		doWait(5000);

		initCPUCores();
		
		
		Ontology taskOntontology = TaskOntology.getInstance();
		Ontology agentManagementOntontology = AgentManagementOntology.getInstance();
		
		MessageTemplate reqMsgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol(
					FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.and(
							MessageTemplate.MatchLanguage(codec.getName()),
							MessageTemplate.or(
								MessageTemplate.MatchOntology(taskOntontology.getName()),
								MessageTemplate.MatchOntology(agentManagementOntontology.getName())
							))));

		addBehaviour(new AchieveREResponder(this, reqMsgTemplate) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ACLMessage handleRequest(ACLMessage request)
					throws NotUnderstoodException, RefuseException {
				try {
					Action a = (Action) getContentManager().extractContent(
							request);

					/**
					 * Task action
					 */
					if (a.getAction() instanceof ExecuteTask) {
						return respondToExecuteTask(request, a);
					}

				} catch (OntologyException e) {
					logError("Problem extracting content: " + e.getMessage(), e);
				} catch (CodecException e) {
					logError("Codec problem: " + e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logError("Failure responding to request: "
						+ request.getContent());
				return failure;
			}

		});

	}

	protected ACLMessage respondToExecuteTask(ACLMessage request, Action a) {

		ExecuteTask executeTask = (ExecuteTask) a.getAction();

		TaskToSolve taskToSolve = new TaskToSolve(
				executeTask.getTask(), a, request);
		waitingToStartComputingTasks.addLast(taskToSolve);
		plan();

		//TODO:
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK - ExecuteTask msg accepted");

		return null;
	}

	public void respondToFinishedTask(ACLMessage finishedTaskMsg) {

		Result result = null;
		try {
			result = (Result) getContentManager().extractContent(
					finishedTaskMsg);
		} catch (UngroundedException e1) {
			logError(e1.getMessage(), e1);
		} catch (CodecException e1) {
			logError(e1.getMessage(), e1);
		} catch (OntologyException e1) {
			logError(e1.getMessage(), e1);
		}
		
		Task finishedTask = (Task) result.getValue();

		CPUCore cpuCore = new CPUCore(finishedTaskMsg.getSender(),
				finishedTask.getCpuCoreID());

		TaskToSolve taskToSolve = getComputingTask(finishedTask.getGraphId());

		computingCores.remove(cpuCore);
		untappedCores.add(cpuCore);

		computingTasks.remove(taskToSolve);

		//plan();

		/////
		ACLMessage msgToManager = taskToSolve.getMsg().createReply();
		msgToManager.setPerformative(ACLMessage.INFORM);
		msgToManager.setLanguage(new SLCodec().getName());
		msgToManager.setOntology(TaskOntology.getInstance().getName());
		
		Result executeResult = new Result(taskToSolve.getAction(), finishedTask);
		try {
			getContentManager().fillContent(msgToManager, executeResult);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}
		send(msgToManager);
		/////
		
		//ACLMessage reply = finishedTaskMsg.createReply();
		//reply.setPerformative(ACLMessage.INFORM);
		//reply.setContent("OK - FinishedTask msg recieved");
	}

	private void plan() {

		TaskToSolve task = waitingToStartComputingTasks.get(0);
		// TODO: improve selection - by priority and workflow

		CPUCore selectedCore = untappedCores.get(0); // TODO: improve selection

		waitingToStartComputingTasks.remove(task);
		computingTasks.add(task);

		computingCores.put(selectedCore, task);
		untappedCores.remove(selectedCore);

		PlannerCommunicator communicator = new PlannerCommunicator(this);
		communicator.sendExecuteTask(task.getTask(), selectedCore.getAID());

	}

	private TaskToSolve getComputingTask(int taskID) {

		for (TaskToSolve taskI : computingTasks) {
			if (taskI.getTask().getGraphId() == taskID) {
				return taskI;
			}
		}

		return null;
	}

}
