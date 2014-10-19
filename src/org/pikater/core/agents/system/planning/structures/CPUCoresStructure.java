package org.pikater.core.agents.system.planning.structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.agents.system.Agent_Planner;
import org.pikater.core.agents.system.planning.PlannerCommunicator;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.task.Task;

/**
 * 
 * Data-model represents sets of CPU Cores
 *
 */
public class CPUCoresStructure {
	private Map <CPUCore, TaskToSolve> busyCores =
			new HashMap<CPUCore, TaskToSolve>();
	private List<CPUCore> untappedCores =
			new ArrayList<CPUCore>();
	
	/**
	 * CPU Cores initialization
	 * 
	 */
	public void initNewCPUCores(Agent_Planner agent,
			List<AID> slaveServers) {

		for (AID managerAgentI : slaveServers) {
			
			PlannerCommunicator comminicator = new PlannerCommunicator(agent);
			ComputerInfo computerInfo =
					comminicator.getComputerInfo(managerAgentI);
			int numberOfCores = computerInfo.getNumberOfCores();
			
			for (int i = 0; i < numberOfCores; i++) {
				
				CPUCore cpuCore = new CPUCore(managerAgentI, i);
				untappedCores.add(cpuCore);
			}
		}
		
	}
	
	/**
	 * Deletes dead CPU Cores - all cores in the dead servers
	 * 
	 * @param agent - {@link Agent_Planner}
	 */
	public Set<TaskToSolve> deleteDeadCPUCores(Agent_Planner agent,
			List<AID> deadSlaveServers) {
		
		// delete untapped cores
		List<CPUCore> cpuCoresToDelete = new ArrayList<CPUCore>();
		for (CPUCore untappedCoreI : untappedCores) {
			AID aidI = untappedCoreI.getAID();
			
			if (deadSlaveServers.contains(aidI)) {
				cpuCoresToDelete.add(untappedCoreI);
			}
		}
		untappedCores.removeAll(cpuCoresToDelete);
		
		List<CPUCore> busyCoresKeys =
				new ArrayList<CPUCore>(this.busyCores.keySet());
		
		Set<TaskToSolve> notFinishedTasks = new HashSet<TaskToSolve>();
		
		// delete busy cores
		for (CPUCore busyCpuCoreI : busyCoresKeys) {
			AID aidI = busyCpuCoreI.getAID();
			
			if (deadSlaveServers.contains(aidI)) {

				TaskToSolve taskToSolveI = this.busyCores.get(busyCpuCoreI);
				notFinishedTasks.add(taskToSolveI);
				
				this.busyCores.remove(busyCpuCoreI);
			}
		}
		
		return notFinishedTasks;
	}
	
	
	/**
	 * Sets a concrete CPU as free
	 *  
	 */
	public TaskToSolve setCPUCoreAsFree(CPUCore cpuCore) {
		
		if (cpuCore == null) {
			throw new IllegalArgumentException("Argument cpuCore can't be null");
		}
		
		TaskToSolve taskToSolve = this.busyCores.get(cpuCore);
		if (taskToSolve == null) {
			throw new IllegalStateException("Structure doesn't contain "
					+ "any task for argument cpuCore");
		}
		
		this.busyCores.remove(cpuCore);
		this.untappedCores.add(cpuCore);
		
		return taskToSolve;
	}
	
	/**
	 * Sets a concrete CPU as busy
	 * 
	 */
	public void setCPUCoreAsBusy(CPUCore selectedCore, TaskToSolve task) {
		
		if (selectedCore == null) {
			throw new IllegalArgumentException("Argument selectedCore can't be null");
		}
		if (task == null) {
			throw new IllegalArgumentException("Argument task can't be null");
		}
		if (! this.untappedCores.contains(selectedCore)) {
			throw new IllegalStateException("SelectedCore is not member of untappedCores");
		}
		
		this.busyCores.put(selectedCore, task);
		this.untappedCores.remove(selectedCore);
	}

	/**
	 * Get the best CPU Core for the Task
	 * 
	 * @param task - Task to solve
	 */
	public CPUCore getTheBestCPUCoreForTask(TaskToSolve task,
			DataFiles dataLocations) {
		
		if (task == null) {
			throw new IllegalArgumentException("Argument task can't be null");
		}
		
		if (untappedCores.isEmpty()) {
			return null;
		}
		
		// select first CPU where is one needed file 
		for (CPUCore cpuCoreI : untappedCores) {
			AID aidCPU = cpuCoreI.getAID();
			for (DataFile dataFileI : dataLocations.getDataFiles()) {
				Set<AID> aidFileLocations = dataFileI.getLocations();
			
				if (aidFileLocations.contains(aidCPU)) {
					return cpuCoreI;
				}
			}
		}
		
		return untappedCores.get(0);
	}
	
	/**
	 * Get CPU Core which is computing concrete Task (TaskID)
	 * 
	 */
	public CPUCore getCPUCoreOfComputingTask(Task task) {
		
		List<CPUCore> cpuCores = new ArrayList<CPUCore>();
		cpuCores.addAll(this.busyCores.keySet());
		for (CPUCore cpuCoreI : cpuCores) {
			TaskToSolve taskToSolveI = this.busyCores.get(cpuCoreI);
			Task taskI = taskToSolveI.getTask();
			
			if (taskI.equalsTask(task)) {
				return cpuCoreI;
			}
		}
		return null;
	}

	/**
	 * Get {@link TaskToSolve} for the concrete {@link Task}
	 * 
	 */
	public TaskToSolve getTaskToSolveOfComputingTask(Task task) {
		
		List<CPUCore> cpuCores = new ArrayList<CPUCore>();
		cpuCores.addAll(this.busyCores.keySet());
		for (CPUCore cpuCoreI : cpuCores) {
			TaskToSolve taskToSolveI = this.busyCores.get(cpuCoreI);
			Task taskI = taskToSolveI.getTask();
			
			if (taskI.equalsTask(task)) {
				
				return taskToSolveI;
			}
		}
		return null;
	}
	
	/**
	 * Get {@link TaskToSolve} by the taskID
	 * 
	 */
	public TaskToSolve getComputingTask(int taskID) {
	
		for (TaskToSolve taskToSolveI : this.busyCores.values()) {
			if (taskToSolveI.getTask().getBatchID() == taskID) {
				return taskToSolveI;
			}
		}
		return null;
	}

	/**
	 * Get number of busy CPU Cores in the structure
	 * 
	 */
	public int getNumOfBusyCores() {
		return this.busyCores.size();
	}
	
	/**
	 * Get number of untapped CPU Cores in the structure
	 * 
	 */
	public int getNumOfUntappedCores() {
		return this.untappedCores.size();
	}
	
	/**
	 * Check existence of a untapped CPU Core
	 */
	public boolean isExistingUntappedCore() {
		return getNumOfUntappedCores() > 0;
	}
	
}
