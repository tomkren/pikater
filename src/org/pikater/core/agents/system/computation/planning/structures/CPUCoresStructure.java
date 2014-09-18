package org.pikater.core.agents.system.computation.planning.structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.agents.system.Agent_Planner;
import org.pikater.core.agents.system.computation.planning.PlannerCommunicator;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.task.Task;

public class CPUCoresStructure {
	private Map <CPUCore, TaskToSolve> busyCores =
			new HashMap<CPUCore, TaskToSolve>();
	private List<CPUCore> untappedCores =
			new ArrayList<CPUCore>();
	
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
	public Set<TaskToSolve> deleteDeadCPUCores(Agent_Planner agent,
			List<AID> deadSlaveServers) {
		
		List<CPUCore> busyCoresKeys =
				new ArrayList<CPUCore>(this.busyCores.keySet());
		
		// delete untapped cores
		for (CPUCore untappedCoreI : untappedCores) {
			AID aidI = untappedCoreI.getAID();
			
			if (deadSlaveServers.contains(aidI)) {
				this.busyCores.remove(untappedCoreI);
			}
		}
		
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
	
	public CPUCore getTheBestCPUCoreForTask(TaskToSolve task,
			DataFiles dataLocations) {
		
		if (task == null) {
			throw new IllegalArgumentException("Argument task can't be null");
		}
		
		if (untappedCores.isEmpty())
		{
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
	
	public TaskToSolve getComputingTask(int taskID) {
	
		for (TaskToSolve taskToSolveI : this.busyCores.values()) {
			if (taskToSolveI.getTask().getBatchID() == taskID) {
				return taskToSolveI;
			}
		}
		return null;
	}

	public int getNumOfBusyCores() {
		return this.busyCores.size();
	}
	public int getNumOfUntappedCores() {
		return this.untappedCores.size();
	}
	
	public boolean isExistingUntappedCore() {
		return getNumOfUntappedCores() > 0;
	}
	
}
