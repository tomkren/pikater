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
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.agents.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.planner.CPUCore;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.agents.system.planner.TaskToSolve;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.BatchOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.FinishedTask;
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
	public java.util.List<Ontology> getOntologies() {
		java.util.List<Ontology> ontologies = new java.util.ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		return ontologies;
	}


	private void initCPUCores() {
		
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
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			logError("Sleep error");
			e1.printStackTrace();
		}
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
					if (a.getAction() instanceof FinishedTask) {
						return respondToFinishedTask(request, a);
					}

				} catch (OntologyException e) {
					e.printStackTrace();
					logError("Problem extracting content: " + e.getMessage());
				} catch (CodecException e) {
					e.printStackTrace();
					logError("Codec problem: " + e.getMessage());
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

	protected ACLMessage respondToFinishedTask(ACLMessage request, Action a) {

		FinishedTask finishedTask = (FinishedTask) a.getAction();

		CPUCore cpuCore = new CPUCore(request.getSender(),
				finishedTask.getCpuCoreID());

		TaskToSolve task = getComputingTask(finishedTask.getTaskID());

		computingCores.remove(cpuCore);
		untappedCores.add(cpuCore);

		computingTasks.remove(task);

		plan();

		/////
		ACLMessage msgToManager = task.getMsg().createReply();
		msgToManager.setLanguage(new SLCodec().getName());
		msgToManager.setOntology(BatchOntology.getInstance().getName());
		
		Result executeResult = new Result(task.getAction(), task.getTask());
		try {
			getContentManager().fillContent(msgToManager, executeResult);
		} catch (CodecException e) {
			logError(e.getMessage());
			e.printStackTrace();
		} catch (OntologyException e) {
			logError(e.getMessage());
			e.printStackTrace();
		}
		send(msgToManager);
		/////
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK - FinishedTask msg recieved");

		// TODO: send info to manager - add behaviour?
		return reply;
	}

	private void plan() {

		TaskToSolve task = waitingToStartComputingTasks.get(0);
		// TODO: improve selection - by priority and workflow

		CPUCore selectedCore = untappedCores.get(0); // TODO: improve selection

		waitingToStartComputingTasks.remove(task);
		computingTasks.add(task);

		computingCores.put(selectedCore, task);
		untappedCores.remove(selectedCore);

		PlannerCommunicator communiocator = new PlannerCommunicator(this);
		communiocator.sendExecuteTask(task.getTask(), selectedCore.getAID());

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
