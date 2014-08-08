package org.pikater.core.agents.system.planner.dataStructures;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pikater.core.agents.system.Agent_ManagerAgent;
import org.pikater.core.agents.system.Agent_Planner;
import org.pikater.core.agents.system.planner.PlannerCommunicator;
import org.pikater.core.ontology.subtrees.management.ComputerInfo;

public class CPUCoresStructure {

	private Map <CPUCore, TaskToSolve> busyCores;
	private List<CPUCore> untappedCores;
	
	public CPUCoresStructure() {
		
		this.busyCores = new HashMap<CPUCore, TaskToSolve>();
		this.untappedCores = new ArrayList<CPUCore>();
	}
	
	public void initCPUCores(Agent_Planner agent) {

		 DFAgentDescription template = new DFAgentDescription();
		 ServiceDescription sd = new ServiceDescription();
		 sd.setType(Agent_ManagerAgent.class.getName());
		 template.addServices(sd);
		 
		 DFAgentDescription[] result = null;
		 try {
			result = DFService.search(agent, template);
		} catch (FIPAException e1) {
			agent.logError(e1.getMessage(), e1);
		}
		if (result.length == 0) {
			return;
		}
		
		List<DFAgentDescription> descriptions =
				new ArrayList<DFAgentDescription>(Arrays.asList(result));

		for (DFAgentDescription descriptionI : descriptions) {
			
			AID managerAgentAID = descriptionI.getName();
			
			PlannerCommunicator comminicator = new PlannerCommunicator(agent);
			ComputerInfo computerInfo =
					comminicator.getComputerInfo(managerAgentAID);
			int numberOfCores = computerInfo.getNumberOfCores();
			
			for (int i = 0; i < numberOfCores; i++) {
				
				CPUCore cpuCore = new CPUCore(managerAgentAID, i);
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
			List<Object> recommendLocalitons) {
		
		if (task == null) {
			throw new IllegalArgumentException("Argument task can't be null");
		}
		
		if (untappedCores.size() == 0) {
			return null;
		}
		return untappedCores.get(0);
	}
	
	public TaskToSolve getComputingTask(int taskID) {
	
		for (TaskToSolve taskToSolveI : busyCores.values()) {
			if (taskToSolveI.getTask().getGraphID() == taskID) {
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
