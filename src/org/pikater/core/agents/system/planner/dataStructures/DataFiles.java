package org.pikater.core.agents.system.planner.dataStructures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class DataFiles {

	private Set<DataFile> dataFiles;
	
	public DataFiles() {
		this.dataFiles = new HashSet<>();
	}

	public Set<DataFile> getDataFiles() {
		return dataFiles;
	}
	public void addDataFile(DataFile dataFile) {
		if (dataFile == null) {
			throw new IllegalArgumentException("Argument dataFile can't be null");
		}
		this.dataFiles.add(dataFile);
	}	
	
	public boolean existsAllDataFiles() {
		
		for (DataFile dataFileI : new ArrayList<DataFile>(dataFiles)) {
			Set<AID> locationsI = dataFileI.getLocations();
			if (locationsI.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	public Set<TaskToSolve> tasksToRestart() {
		
		Set<TaskToSolve> tasksToSolve = new HashSet<>();
		
		for (DataFile dataFileI : new ArrayList<DataFile>(dataFiles)) {
			Set<AID> locationsI = dataFileI.getLocations();
			TaskToSolve taskToSolveI = dataFileI.getProducer();
			if (locationsI.isEmpty()) {
				taskToSolveI.setSendResultToManager(false);
				tasksToSolve.add(taskToSolveI);
			}
		}
		return tasksToSolve;
	}
	
}
