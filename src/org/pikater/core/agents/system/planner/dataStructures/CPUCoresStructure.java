package org.pikater.core.agents.system.planner.dataStructures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.agents.system.Agent_Planner;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;
import org.pikater.core.ontology.subtrees.task.Task;

public class CPUCoresStructure {
	private Map <CPUCore, TaskToSolve> busyCores = new HashMap<CPUCore, TaskToSolve>();
	private List<CPUCore> untappedCores = new ArrayList<CPUCore>();
	
	public void initCPUCores(Agent_Planner agent,
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
	
	// TODO: improve selection
	public CPUCore getTheBestCPUCoreForTask(TaskToSolve task,
			Set<String> dataLocations) {
		
		if (task == null) {
			throw new IllegalArgumentException("Argument task can't be null");
		}
		
		if (untappedCores.size() == 0) {
			return null;
		}
		return untappedCores.get(0);
	}
	
	public CPUCore getCPUCoreOfComputingTask(Task task) {
		
		List<CPUCore> cpuCores = new ArrayList<CPUCore>();
		cpuCores.addAll(busyCores.keySet());
		for (CPUCore cpuCoreI : cpuCores) {
			TaskToSolve taskToSolveI = busyCores.get(cpuCoreI);
			Task taskI = taskToSolveI.getTask();
			
			if (taskI.equalsTask(task)) {
				return cpuCoreI;
			}
		}
		return null;
	}

	public TaskToSolve getTaskToSolveOfComputingTask(Task task) {
		
		List<CPUCore> cpuCores = new ArrayList<CPUCore>();
		cpuCores.addAll(busyCores.keySet());
		for (CPUCore cpuCoreI : cpuCores) {
			TaskToSolve taskToSolveI = busyCores.get(cpuCoreI);
			Task taskI = taskToSolveI.getTask();
			
			if (taskI.equalsTask(task)) {
				return taskToSolveI;
			}
		}
		return null;
	}
	
	public TaskToSolve getComputingTask(int taskID) {
	
		for (TaskToSolve taskToSolveI : busyCores.values()) {
			if (taskToSolveI.getTask().getBatchID() == taskID) {
				return taskToSolveI;
			}
		}
		return null;
	}

	public int getNumOfBusyCores() {
		return busyCores.size();
	}
	public int getNumOfUntappedCores() {
		return untappedCores.size();
	}
}
