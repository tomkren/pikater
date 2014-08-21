package org.pikater.core.agents.system.planner.dataStructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.task.Task;
import org.pikater.core.ontology.subtrees.task.TaskOutput;

public class DataRegistry {
	private static String MASTER_NODE_NAME = "$master";
	/** data hash => Set(node id) */
	private Map<String,Set<String>> dataMap = new HashMap<>();

	public DataRegistry(CPUCoresStructure cpuCoresStructure) {
		// TODO initial load, currently presumes no data is anywhere initially
	}

	public void saveDataLocation(Task task, String nodeName) {
		if (nodeName == null) {
			nodeName = MASTER_NODE_NAME;
		}
		for (TaskOutput outputTaskI : task.getOutput()) {
			String hash = outputTaskI.getName();
			if (dataMap.get(hash) == null) {
				Set<String> list = new HashSet<>();
				list.add(nodeName);
				dataMap.put(hash, list);
			} else {
				dataMap.get(hash).add(nodeName);
			}
		}
		// data that had to be transported on the slave server
		for (Data inputDataI : task.getDatas().getDatas()) {
			String hash = inputDataI.getInternalFileName();
			if (dataMap.get(hash) == null) {
				Set<String> list = new HashSet<>();
				list.add(nodeName);
				dataMap.put(hash, list);
			} else {
				dataMap.get(hash).add(nodeName);
			}
		}
		printMap();
	}

	public Set<String> getDataLocations(TaskToSolve task) {
		printMap();
		List<Data> data = task.getTask().getDatas().getDatas();
		if (data.size() == 0) {
			return null;
		}
		Set<String> res = dataMap.get(data.get(0).getInternalFileName());
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