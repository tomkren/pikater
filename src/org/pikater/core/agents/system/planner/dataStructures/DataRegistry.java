package org.pikater.core.agents.system.planner.dataStructures;

import jade.core.AID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

public class DataRegistry {
	/** data hash => Set(node AID) */
	private Map<String,Set<AID>> dataMap = new HashMap<>();
	private PikaterAgent agent;

	public DataRegistry(PikaterAgent agent, CPUCoresStructure cpuCoresStructure) {
		this.agent = agent;
		// TODO initial load, currently presumes no data is anywhere initially
	}

	public void saveDataLocation(Task task, AID slaveServerAID) {
		if (slaveServerAID == null) {
			slaveServerAID = agent.getAID();
		}
		for (TaskOutput outputTaskI : task.getOutput()) {
			String hash = outputTaskI.getName();
			if (dataMap.get(hash) == null) {
				Set<AID> list = new HashSet<>();
				list.add(slaveServerAID);
				dataMap.put(hash, list);
			} else {
				dataMap.get(hash).add(slaveServerAID);
			}
		}
		// data that had to be transported on the slave server
		for (Data inputDataI : task.getDatas().getDatas()) {
			String hash = inputDataI.getInternalFileName();
			if (dataMap.get(hash) == null) {
				Set<AID> list = new HashSet<>();
				list.add(slaveServerAID);
				dataMap.put(hash, list);
			} else {
				dataMap.get(hash).add(slaveServerAID);
			}
		}
		printMap();
	}

	public Set<AID> getDataLocations(TaskToSolve task) {
		printMap();
		List<Data> data = task.getTask().getDatas().getDatas();
		if (data.size() == 0) {
			return null;
		}
		Set<AID> res = dataMap.get(data.get(0).getInternalFileName());
		if (res == null) {
			return new HashSet<>();
		}
		res = new HashSet<>(res);
		for (Data d : data) {
			res.retainAll(dataMap.get(d.getInternalFileName()));
		}
		return res;
	}
	
	private void printMap() {
		System.out.println("Data map:");
		for (String hash : dataMap.keySet()) {
			System.out.println("  "+hash+" ==> "+dataMap.get(hash).toString());
		}
	}
}