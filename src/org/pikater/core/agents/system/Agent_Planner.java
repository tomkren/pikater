package org.pikater.core.agents.system;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import org.pikater.core.AgentNames;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.agents.system.planner.dataStructures.CPUCore;
import org.pikater.core.agents.system.planner.dataStructures.CPUCoresStructure;
import org.pikater.core.agents.system.planner.dataStructures.DistributedData;
import org.pikater.core.agents.system.planner.dataStructures.TaskToSolve;
import org.pikater.core.agents.system.planner.dataStructures.WaitingTasksQueues;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.Task;


public class Agent_Planner extends PikaterAgent {
	
	private static final long serialVersionUID = 820846175393846627L;

	private WaitingTasksQueues waitingToStartComputingTasks =
			new WaitingTasksQueues();
	private CPUCoresStructure cpuCoresStructure =
			new CPUCoresStructure();
	private DistributedData distributedData =
			new DistributedData();
	
	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		return ontologies;
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

	private void initCPUCores() {
		cpuCoresStructure.initCPUCores(this);
	}
	
	protected ACLMessage respondToExecuteTask(ACLMessage request, Action a) {

		ExecuteTask executeTask = (ExecuteTask) a.getAction();

		TaskToSolve taskToSolve = new TaskToSolve(
				executeTask.getTask(), a, request);
		
		
		synchronized(Agent_Planner.class) {
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
		}

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
		finishedTask.setFinish(Agent_DataManager.getCurrentPikaterDateString());
		
		CPUCore cpuCore = new CPUCore(
				finishedTaskMsg.getSender(), finishedTask.getCpuCoreID());

		TaskToSolve taskToSolve = cpuCoresStructure.getComputingTask(
				finishedTask.getGraphId());

		synchronized(Agent_Planner.class) {
			this.cpuCoresStructure.setCPUCoreAsFree(cpuCore);
			this.distributedData.saveLocationOfData(finishedTask);
			plan();
		}
		
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

		TaskToSolve task = waitingToStartComputingTasks.
				removeTaskWithHighestPriority();
		
		// test if some task is available
		if (task == null) {
			//this.logError("Any task available");
			return;
		}
		
		task.getTask().setStart(Agent_DataManager.getCurrentPikaterDateString());

		List<Object> recommendLocalitons = this.distributedData.
				recommendCountingLocality(task);
		CPUCore selectedCore = this.cpuCoresStructure.
				getTheBestCPUCoreForTask(task, recommendLocalitons);

		// test if some core is available
		if (selectedCore == null) {
			//this.logError("Any core available");
			this.waitingToStartComputingTasks.addTask(task);
			return;
		}
		
		this.cpuCoresStructure.setCPUCoreAsBusy(selectedCore, task);

		PlannerCommunicator communicator = new PlannerCommunicator(this);
		communicator.sendExecuteTask(task.getTask(), selectedCore.getAID());
	}


}
