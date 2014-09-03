package org.pikater.core.agents.system;

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pikater.core.AgentNames;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.metadata.MetadataService;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.agents.system.planner.dataStructures.CPUCore;
import org.pikater.core.agents.system.planner.dataStructures.CPUCoresStructure;
import org.pikater.core.agents.system.planner.dataStructures.DataFiles;
import org.pikater.core.agents.system.planner.dataStructures.DataRegistry;
import org.pikater.core.agents.system.planner.dataStructures.Lock;
import org.pikater.core.agents.system.planner.dataStructures.SlaveServersStructure;
import org.pikater.core.agents.system.planner.dataStructures.TaskToSolve;
import org.pikater.core.agents.system.planner.dataStructures.WaitingTasksQueues;
import org.pikater.core.ontology.AgentManagementOntology;
import org.pikater.core.ontology.DataOntology;
import org.pikater.core.ontology.MetadataOntology;
import org.pikater.core.ontology.ModelOntology;
import org.pikater.core.ontology.TaskOntology;
import org.pikater.core.ontology.subtrees.batch.BatchPriorityChanged;
import org.pikater.core.ontology.subtrees.dataset.SaveDataset;
import org.pikater.core.ontology.subtrees.systemLoad.GetSystemLoad;
import org.pikater.core.ontology.subtrees.systemLoad.SystemLoad;
import org.pikater.core.ontology.subtrees.task.ExecuteTask;
import org.pikater.core.ontology.subtrees.task.KillTasks;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;


public class Agent_Planner extends PikaterAgent {
	
	private static final long serialVersionUID = 820846175393846627L;

	private volatile Lock lock = new Lock();
	
	private SlaveServersStructure slaveServersStructure =
			new SlaveServersStructure();
	private WaitingTasksQueues waitingToStartComputingTasks =
			new WaitingTasksQueues();
	private CPUCoresStructure cpuCoresStructure =
			new CPUCoresStructure();
	private DataRegistry dataRegistry =
			new DataRegistry(this, cpuCoresStructure);
	
	@Override
	public List<Ontology> getOntologies() {
		List<Ontology> ontologies = new ArrayList<Ontology>();
		ontologies.add(DataOntology.getInstance());
		ontologies.add(TaskOntology.getInstance());
		ontologies.add(MetadataOntology.getInstance());
		ontologies.add(AgentManagementOntology.getInstance());
		ontologies.add(ModelOntology.getInstance());
		return ontologies;
	}
	
	protected void setup() {
		initDefault();

		registerWithDF(AgentNames.PLANNER);

		// waiting to start ManagerAgent
		doWait(10000);
		
		
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
					
					if (a.getAction() instanceof BatchPriorityChanged) {
						return respondToBatchPriorityChanged(request, a);
					}
					
					if (a.getAction() instanceof GetSystemLoad) {
						return respondToGetSystemLoad(request, a);
					}
					
					if (a.getAction() instanceof KillTasks) {
						return respondToKillTasks(request, a);
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

		//dataRegistry.updateDataSets();
	}
	
	protected ACLMessage respondToExecuteTask(ACLMessage request, Action a) {

		ExecuteTask executeTask = (ExecuteTask) a.getAction();

		TaskToSolve taskToSolve = new TaskToSolve(
				executeTask.getTask(), a, request);
		taskToSolve.setSendResultToManager(true);
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logError(e.getMessage(), e);
		}
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
		lock.unlock();

		
		//ACLMessage reply = request.createReply();
		//reply.setPerformative(ACLMessage.INFORM);
		//reply.setContent("OK - ExecuteTask msg accepted");

		return null;
	}

	protected ACLMessage respondToBatchPriorityChanged(ACLMessage request, Action a) {
		
		BatchPriorityChanged batchPriorityChanged =
				(BatchPriorityChanged) a.getAction();
		int batchID = batchPriorityChanged.getBatchID();
		
		int newBatchPriority = DataManagerService.getBatchPriority(this, batchID);
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logError(e.getMessage(), e);
		}
			waitingToStartComputingTasks.updateTaskPriority(
					batchID, newBatchPriority);
		lock.unlock();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK - Priority changed");
		
		return reply;
	}
	
	protected ACLMessage respondToGetSystemLoad(ACLMessage request, Action a) {

		SystemLoad systemLoad = getSystemLoad();
		
		ACLMessage msgSystemLoad = request.createReply();
		msgSystemLoad.setPerformative(ACLMessage.INFORM);
		msgSystemLoad.setLanguage(new SLCodec().getName());
		msgSystemLoad.setOntology(TaskOntology.getInstance().getName());
		
		Result executeResult = new Result(a, systemLoad);
		try {
			getContentManager().fillContent(msgSystemLoad, executeResult);
		} catch (CodecException e) {
			logError(e.getMessage(), e);
		} catch (OntologyException e) {
			logError(e.getMessage(), e);
		}
		
		return msgSystemLoad;
	}
	
	private SystemLoad getSystemLoad() {
		
		SystemLoad systemLoad = new SystemLoad();
		systemLoad.setNumberOfBusyCores(
				cpuCoresStructure.getNumOfBusyCores());
		systemLoad.setNumberOfUntappedCores(
				cpuCoresStructure.getNumOfUntappedCores());
		systemLoad.setNumberOfTasksInQueue(
				waitingToStartComputingTasks.getNumberOfTasksInQueue());
		systemLoad.print();
		
		return systemLoad;
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

		CPUCore cpuCore = cpuCoresStructure.
				getCPUCoreOfComputingTask(finishedTask);
		TaskToSolve taskToSolve = cpuCoresStructure.
				getTaskToSolveOfComputingTask(finishedTask);
		// update task - result
		taskToSolve.setTask(finishedTask);

		try {
			lock.lock();
		} catch (InterruptedException e1) {
			logError(e1.getMessage(), e1);
		}
			cpuCoresStructure.setCPUCoreAsFree(cpuCore);
			String node = nodeName(cpuCore.getAID());
			dataRegistry.saveDataLocation(taskToSolve, cpuCore.getAID());
			saveDataToDB(finishedTask, node);
			plan();
		lock.unlock();
		
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
		
		if (taskToSolve.isSendResultToManager()) {
			send(msgToManager);
		}
		/////
		
		//ACLMessage reply = finishedTaskMsg.createReply();
		//reply.setPerformative(ACLMessage.INFORM);
		//reply.setContent("OK - FinishedTask msg recieved");
	}

	private void saveDataToDB(Task task, String node) {
		String dataManagerName = AgentNames.DATA_MANAGER;
		if (node != null) {
			dataManagerName += "-";
			dataManagerName += node;
		}
		AID dataManager = new AID(dataManagerName, false);
		Ontology ontology = DataOntology.getInstance();
		
		int userID = task.getUserID();
		
		for (TaskOutput t : task.getOutput()) {
			log("requesting save of data "+t.getName());
			SaveDataset sd = new SaveDataset();
			sd.setUserID(task.getUserID());
			String savedFileName = CoreConfiguration.DATA_FILES_PATH+System.getProperty("file.separator")+t.getName();
			sd.setSourceFile(savedFileName);			
			sd.setDescription("Output from batch "+task.getBatchID()+ " ("+t.getType().toString()+")");
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(dataManager);
			request.setLanguage(getCodec().getName());
			request.setOntology(ontology.getName());
			ACLMessage reply = null;
			int computedDataID;
			try {
				getContentManager().fillContent(request, new Action(dataManager, sd));
				reply = FIPAService.doFipaRequestClient(this, request);
				if (reply == null) {
					logError("Failed to save output data in DB - reply not received."
							 +" (Source file: "+savedFileName+")");
					return;
				}
				computedDataID = (Integer)reply.getContentObject();
			} catch (CodecException | OntologyException | FIPAException e) {
				logError("Failed to save output data in DB", e);
				return;
			} catch (UnreadableException e) {
				logError("Failed to request metadata", e);
				return;
			}
			MetadataService.requestMetadataForComputedData(this,
					computedDataID, userID);
			log("saved output to DB: "+t.getName());
		}
	}

	private static String nodeName(AID aid) {
		String name = aid.getLocalName();
		if (name.contains("-")) {
			return name.substring(name.lastIndexOf('-')+1);
		} else {
			return null;
		}
	}

	private ACLMessage respondToKillTasks(ACLMessage request, Action a) {
		
		KillTasks killTasks = (KillTasks) a.getAction();
		int batchID = killTasks.getBatchID();
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logError(e.getMessage(), e);
		}
			this.waitingToStartComputingTasks.removeTasks(batchID);
		lock.unlock();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}
	
	private void plan() {

		// test if some task is available
		if (! waitingToStartComputingTasks.isExistingTaskToSolve()) {
			return;
		}
		
		// choose one task(with the highest priority)
		TaskToSolve taskToSolve = this.waitingToStartComputingTasks.
				removeTaskWithHighestPriority();
		
		Task task = taskToSolve.getTask();
		task.setStart(Agent_DataManager.getCurrentPikaterDateString());
		
		// update number of cores
		List<AID> newSlaveServers =
				slaveServersStructure.checkForNewSlaveServers(this);
		List<AID> deadSlaveServers =
				slaveServersStructure.checkForDeadSlaveServers(this);
		// initialization of new CPU Cores
		cpuCoresStructure.initNewCPUCores(this, newSlaveServers);
		// removing dead CPU Cores
		Set<TaskToSolve> notFinishedTasks =
				cpuCoresStructure.deleteDeadCPUCores(this, deadSlaveServers);
		
		// add back to queue all not finished Tasks and restart function plan
		if (! notFinishedTasks.isEmpty()) {
			log("Some SlaveServer is dead");
			waitingToStartComputingTasks.addTasks(notFinishedTasks);
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
			return;
		}


		//delete CPU cores from dataRegistry
		dataRegistry.deleteDeadCPUCores(deadSlaveServers);
		dataRegistry.updateDataSets();
		// get location of all files needed to solve the Task
		DataFiles dataLocations = dataRegistry.
				getDataLocations(taskToSolve);

		// add back to queue all needed Tasks and restart function plan
		if (! dataLocations.existsAllDataFiles()) {
			log("All Data doesn't exists");
			Set<TaskToSolve> taskToRestart = dataLocations.tasksToRestart();
			waitingToStartComputingTasks.addTasks(taskToRestart);
			
			taskToSolve.setDownPriority();
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
			return;
		}

		// test if some core is available
		if (! cpuCoresStructure.isExistingUntappedCore()) {
			this.waitingToStartComputingTasks.addTask(taskToSolve);
			return;
		}
		// select the best core
		CPUCore selectedCore = cpuCoresStructure.
				getTheBestCPUCoreForTask(taskToSolve, dataLocations);
		
		// set CPU core as busy
		this.cpuCoresStructure.setCPUCoreAsBusy(selectedCore, taskToSolve);

		// send task to the computing agent
		PlannerCommunicator communicator = new PlannerCommunicator(this);
		communicator.sendExecuteTask(task, selectedCore.getAID());
		
		getSystemLoad();
		
	}
}