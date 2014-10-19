package org.pikater.core.agents.system.planning.structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.pikater.core.CoreAgents;
import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.agents.system.planning.structures.DataFile.DataFileType;
import org.pikater.core.ontology.subtrees.data.Data;
import org.pikater.core.ontology.subtrees.data.Datas;
import org.pikater.core.ontology.subtrees.dataset.DatasetInfo;
import org.pikater.core.ontology.subtrees.dataset.DatasetsInfo;
import org.pikater.core.ontology.subtrees.task.TaskOutput;
import org.pikater.shared.logging.core.ConsoleLogger;

/**
 * 
 * Data-model represents positions of the files in the distributed system
 *
 */
public class DataRegistry {
	
	/**
	 * data hash => Set(node AID)
	 */
	private Map<String, DataFile> dataMap = new HashMap<>();
	private PikaterAgent agent;

	/**
	 * Constructor - initialization
	 * 
	 */
	public DataRegistry(PikaterAgent agent,
			CPUCoresStructure cpuCoresStructure) {
		this.agent = agent;
		// TODO initial load, currently presumes no data is anywhere initially
	}

	/**
	 * Update Data-Sets locations in the structure 
	 */
	public void updateDataSets() {
		
		DatasetsInfo datasetsInfo =
				DataManagerService.getAllDatasetInfo(agent);
		for (DatasetInfo datasetInfo : datasetsInfo.getDatasets()) {
			
			AID managerAgentAID =
					new AID(CoreAgents.MANAGER_AGENT.getName(), false);
			String hash = datasetInfo.getHash();

			if (dataMap.get(hash) == null) {
				DataFile dataFileI = new DataFile(DataFileType.DATA_SET);
				dataFileI.setHash(hash);
				dataFileI.setProducer(null);
				dataFileI.addLocation(managerAgentAID);
				
				dataMap.put(hash, dataFileI);
			} else {
				dataMap.get(hash).addLocation(managerAgentAID);
			}
		}
	}
	/**
	 * Save data to concrete location
	 * 
	 */
	public void saveDataLocation(TaskToSolve taskToSolve, AID slaveServerAID) {
		
		for (TaskOutput outputTaskI : taskToSolve.getTask().getOutput()) {
			String hash = outputTaskI.getName();
			if (dataMap.get(hash) == null) {
				
				AID location;
				if (slaveServerAID == null) {
					location = agent.getAID();
				} else {
					location = slaveServerAID;
				}
				
				DataFile file = new DataFile(DataFileType.COMPUTED_DATA);
				file.setHash(hash);
				file.setProducer(taskToSolve);
				file.addLocation(location);
				
				dataMap.put(hash, file);
			} else {
				dataMap.get(hash).addLocation(slaveServerAID);
			}
		}
		printMap();
	}

	/**
	 * Get Locations for the concrete Task
	 * 
	 */
	public DataFiles getDataLocations(TaskToSolve task) {
		
		Datas data = task.getTask().getDatas();
		
		DataFiles dataFiles = new DataFiles();
		
		for (Data dataI : data.getDatas()) {
			
			String fileHashI = dataI.getInternalFileName();
			
			DataFile dataFileI = dataMap.get(fileHashI);
			if (dataFileI == null) {
				throw new IllegalArgumentException("Unknow Data");
			} else {
				dataFiles.addDataFile(dataFileI);
			}
		}
		
		return dataFiles;
	}

	/**
	 * Delete dead CPU Cores
	 * 
	 */
	public void deleteDeadCPUCores(List<AID> deadSlaveServers) {
		
		List<String> hashs = new ArrayList<String>(dataMap.keySet());
		
		for (String hashI : hashs) {
			DataFile dataFileI = dataMap.get(hashI);
			Set<AID> locationsI = dataFileI.getLocations();
			
			for (AID aidI : new ArrayList<AID>(locationsI)) {
				if(deadSlaveServers.contains(aidI)) {
					locationsI.remove(aidI);
				}
			}
		}
	}
	
	/**
	 * Print the map of file location
	 */
	private void printMap() {
		StringBuilder sb = new StringBuilder("Data map:\n");
		for (String hash : dataMap.keySet()) {
			sb.append("\t");
			sb.append(hash);
			sb.append(" ==> ");
			sb.append(dataMap.get(hash).toString());
			sb.append("\n");
		}
		ConsoleLogger.log(Level.INFO, sb.toString()); 
	}
}
