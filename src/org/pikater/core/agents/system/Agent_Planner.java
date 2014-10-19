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

import org.pikater.core.CoreAgents;
import org.pikater.core.CoreConfiguration;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.metadata.MetadataService;
import org.pikater.core.agents.system.planning.PlannerCommunicator;
import org.pikater.core.agents.system.planning.structures.CPUCore;
import org.pikater.core.agents.system.planning.structures.CPUCoresStructure;
import org.pikater.core.agents.system.planning.structures.DataFiles;
import org.pikater.core.agents.system.planning.structures.DataRegistry;
import org.pikater.core.agents.system.planning.structures.Lock;
import org.pikater.core.agents.system.planning.structures.SlaveServersStructure;
import org.pikater.core.agents.system.planning.structures.TaskToSolve;
import org.pikater.core.agents.system.planning.structures.WaitingTasksQueues;
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
import org.pikater.core.utilities.CoreUtilities;

/**
 * 
 * Agent performs the function of Planner Task to distributed slave servers
 * 
 */
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
	
	/**
	 * Get ontologies which is using this agent
	 */
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
	
	/**
	 * Agent setup
	 */
	protected void setup() {
		initDefault();

		registerWithDF(CoreAgents.PLANNER.getName());

		// waiting to start ManagerAgent
		doWait(10000);
		
		
		Ontology taskOntontology = TaskOntology.getInstance();
		Ontology agentManagementOntontology =
				AgentManagementOntology.getInstance();
		
		MessageTemplate reqMsgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol(
					FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.and(
							MessageTemplate.MatchLanguage(codec.getName()),
							MessageTemplate.or(
								MessageTemplate.MatchOntology(
										taskOntontology.getName()),
								MessageTemplate.MatchOntology(
										agentManagementOntontology.getName())
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
					logException("Problem extracting content: " +
						e.getMessage(), e);
				} catch (CodecException e) {
					logException("Codec problem: " + e.getMessage(), e);
				}

				ACLMessage failure = request.createReply();
				failure.setPerformative(ACLMessage.FAILURE);
				logSevere("Failure responding to request: "
						+ request.getContent());
				return failure;
			}

		});

	}
	
	/**
	 * Handle respond to {@link ExecuteTask}
	 * 
	 * @param request {@link ACLMessage} - received message
	 * @param a {@link Action} - {@link ExecuteTask} action
	 * @return {@link ACLMessage}
	 */
	protected ACLMessage respondToExecuteTask(ACLMessage request, Action a) {

		ExecuteTask executeTask = (ExecuteTask) a.getAction();

		TaskToSolve taskToSolve = new TaskToSolve(
				executeTask.getTask(), a, request);
		taskToSolve.setSendResultToManager(true);
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logException(e.getMessage(), e);
		}
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
		lock.unlock();

		
		//ACLMessage reply = request.createReply();
		//reply.setPerformative(ACLMessage.INFORM);
		//reply.setContent("OK - ExecuteTask msg accepted");

		return null;
	}

	/**
	 * Handle respond to {@link BatchPriorityChanged}
	 * 
	 * @param request {@link ACLMessage} - received message
	 * @param a {@link Action} - {@link BatchPriorityChanged} action
	 * @return {@link ACLMessage} - OK inform message
	 */
	protected ACLMessage respondToBatchPriorityChanged(ACLMessage request,
			Action a) {
		
		BatchPriorityChanged batchPriorityChanged =
				(BatchPriorityChanged) a.getAction();
		int batchID = batchPriorityChanged.getBatchID();
		
		int newBatchPriority =
				DataManagerService.getBatchPriority(this, batchID);
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logException(e.getMessage(), e);
		}
			waitingToStartComputingTasks.updateTaskPriority(
					batchID, newBatchPriority);
		lock.unlock();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK - Priority changed");
		
		return reply;
	}
	
	/**
	 * Handle respond to {@link GetSystemLoad}
	 * 
	 * @param request {@link ACLMessage} - received message
	 * @param a {@link Action} - {@link BatchPriorityChanged} action
	 * @return {@link ACLMessage} - contains actual  {@link SystemLoad}
	 */
	protected ACLMessage respondToGetSystemLoad(ACLMessage request,
			Action a) {

		SystemLoad systemLoad = getSystemLoad();
		
		ACLMessage msgSystemLoad = request.createReply();
		msgSystemLoad.setPerformative(ACLMessage.INFORM);
		msgSystemLoad.setLanguage(new SLCodec().getName());
		msgSystemLoad.setOntology(TaskOntology.getInstance().getName());
		
		Result executeResult = new Result(a, systemLoad);
		try {
			getContentManager().fillContent(msgSystemLoad, executeResult);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}
		
		return msgSystemLoad;
	}

	/**
	 * Get system CPU load, contains load of all distributed slave servers
	 * 
	 * @return {@link SystemLoad}
	 */
	private SystemLoad getSystemLoad() {
		
		SystemLoad systemLoad = new SystemLoad();
		systemLoad.setNumberOfBusyCores(
				cpuCoresStructure.getNumOfBusyCores());
		systemLoad.setNumberOfUntappedCores(
				cpuCoresStructure.getNumOfUntappedCores());
		systemLoad.setNumberOfTasksInQueue(
				waitingToStartComputingTasks.getNumberOfTasksInStructure());
		systemLoad.print();
		
		return systemLoad;
	}
	
	/**
	 * Get system CPU load, contains load of all distributed slave servers
	 * 
	 * @return {@link SystemLoad}
	 */
	public void respondToFinishedTask(ACLMessage finishedTaskMsg) {

		Result result = null;
		try {
			result = (Result) getContentManager().extractContent(
					finishedTaskMsg);
		} catch (UngroundedException e1) {
			logException(e1.getMessage(), e1);
			return;
		} catch (CodecException e1) {
			logException(e1.getMessage(), e1);
			return;
		} catch (OntologyException e1) {
			logException(e1.getMessage(), e1);
			return;
		}
		
		Task finishedTask = (Task) result.getValue();
		finishedTask.setFinish(
				CoreUtilities.getCurrentPikaterDateString());

		CPUCore cpuCore = cpuCoresStructure.
				getCPUCoreOfComputingTask(finishedTask);
		TaskToSolve taskToSolve = cpuCoresStructure.
				getTaskToSolveOfComputingTask(finishedTask);
		// update task - result
		taskToSolve.setTask(finishedTask);

		try {
			lock.lock();
		} catch (InterruptedException e1) {
			logException(e1.getMessage(), e1);
		}
			cpuCoresStructure.setCPUCoreAsFree(cpuCore);
			String node = nodeName(cpuCore.getAID());
			dataRegistry.saveDataLocation(taskToSolve, cpuCore.getAID());
			saveDataToDB(finishedTask, node);
			plan();
		lock.unlock();
		
		ACLMessage msgToManager = taskToSolve.getMsg().createReply();
		msgToManager.setPerformative(ACLMessage.INFORM);
		msgToManager.setLanguage(new SLCodec().getName());
		msgToManager.setOntology(TaskOntology.getInstance().getName());
		
		Result executeResult =
				new Result(taskToSolve.getAction(), finishedTask);
		try {
			getContentManager().fillContent(msgToManager, executeResult);
		} catch (CodecException e) {
			logException(e.getMessage(), e);
		} catch (OntologyException e) {
			logException(e.getMessage(), e);
		}
		
		if (taskToSolve.isSendResultToManager()) {
			send(msgToManager);
		}
		
		//ACLMessage reply = finishedTaskMsg.createReply();
		//reply.setPerformative(ACLMessage.INFORM);
		//reply.setContent("OK - FinishedTask msg recieved");
	}

	/**
	 * Send a message to save output of {@link Task}, receiver is agent
	 * {@link Agent_DataManager}
	 * 
	 * @param task {@link Task}
	 * @param node - slave server description
	 */
	private void saveDataToDB(Task task, String node) {
		String dataManagerName = CoreAgents.DATA_MANAGER.getName();
		if (node != null) {
			dataManagerName += "-";
			dataManagerName += node;
		}
		AID dataManager = new AID(dataManagerName, false);
		Ontology ontology = DataOntology.getInstance();
		
		int userID = task.getUserID();
		
		for (TaskOutput taskOutputI : task.getOutput()) {
			logInfo("requesting save of data " + taskOutputI.getName());
			SaveDataset sd = new SaveDataset();
			sd.setUserID(task.getUserID());
			
			String savedFileName =
					CoreConfiguration.getDataFilesPath() +
					System.getProperty("file.separator") +
					taskOutputI.getName();
			
			sd.setSourceFile(savedFileName);			
			sd.setDescription(
					"Output from batch " +
					task.getBatchID() +
					" ("+taskOutputI.getType().toString()+")");
			
			ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(dataManager);
			request.setLanguage(getCodec().getName());
			request.setOntology(ontology.getName());
			ACLMessage reply = null;
			int computedDataID;
			try {
				Action actionI = new Action(dataManager, sd);
				getContentManager().fillContent(request, actionI);
				reply = FIPAService.doFipaRequestClient(this, request);
				if (reply == null) {
					logSevere("Failed to save output data in DB "
							+ "- reply not received."
							+ " (Source file: " + savedFileName+")");
					return;
				}
				computedDataID = (Integer)reply.getContentObject();
			} catch (CodecException | OntologyException | FIPAException e) {
				logException("Failed to save output data in DB", e);
				return;
			} catch (UnreadableException e) {
				logException("Failed to request metadata", e);
				return;
			}
			MetadataService.requestMetadataForComputedData(this,
					computedDataID, userID);
			logInfo("saved output to DB: "+taskOutputI.getName());
		}
	}

	/**
	 * Convert {@link AID} to node name
	 * @param aid {@link AID}
	 * @return String - name of node 
	 */
	private static String nodeName(AID aid) {
		String name = aid.getLocalName();
		if (name.contains("-")) {
			return name.substring(name.lastIndexOf('-')+1);
		} else {
			return null;
		}
	}

	/**
	 * Handle respond to kill all {@link Task} with concrete BatchID
	 * @param request {@link ACLMessage}
	 * @param a {@link Action} - action {@link KillTasks}
	 * @return {@link ACLMessage}
	 */
	private ACLMessage respondToKillTasks(ACLMessage request, Action a) {
		
		KillTasks killTasks = (KillTasks) a.getAction();
		int batchID = killTasks.getBatchID();
		
		try {
			lock.lock();
		} catch (InterruptedException e) {
			logException(e.getMessage(), e);
		}
			this.waitingToStartComputingTasks.removeTasks(batchID);
		lock.unlock();
		
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent("OK");
		return reply;
	}
	
	
	/**
	 * Scheduling of incoming Tasks to free distributed CPUs
	 */
	private void plan() {

		// test if some task is available
		if (! waitingToStartComputingTasks.isExistingTaskToSolve()) {
			return;
		}
		
		// choose one task(with the highest priority)
		TaskToSolve taskToSolve = this.waitingToStartComputingTasks.
				removeTaskWithHighestPriority();
		
		Task task = taskToSolve.getTask();
		task.setStart(CoreUtilities.getCurrentPikaterDateString());
		
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
			logInfo("Some SlaveServer is dead");
			waitingToStartComputingTasks.addTasks(notFinishedTasks);
			waitingToStartComputingTasks.addTask(taskToSolve);
			plan();
			return;
		}


		// delete CPU cores from dataRegistry
		dataRegistry.deleteDeadCPUCores(deadSlaveServers);
		dataRegistry.updateDataSets();
		// get location of all files needed to solve the Task
		DataFiles dataLocations = dataRegistry.
				getDataLocations(taskToSolve);

		// add back to queue all needed Tasks and restart function plan
		if (! dataLocations.existsAllDataFiles()) {
			logInfo("All Data doesn't exists");
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