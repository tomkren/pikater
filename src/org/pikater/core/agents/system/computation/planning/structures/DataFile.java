package org.pikater.core.agents.system.computation.planning.structures;

import jade.core.AID;

import java.util.HashSet;
import java.util.Set;

public class DataFile {

	public static enum DataFileType
	{
		DATA_SET,
		COMPUTED_DATA
	}
	
	private String hash;
	private DataFileType type;
	private Set<AID> locations;
	private TaskToSolve producer;

	public DataFile(DataFileType type) {
		this.type = type;
		this.locations = new HashSet<>();
	}
	
	public DataFile(DataFileType type, Set<AID> locations) {
		this.type = type;
		this.locations = locations;
	}
	public DataFile(DataFileType type, AID location) {
		this.type = type;
		this.locations = new HashSet<>();
		this.locations.add(location);
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

	public DataFileType getType() {
		return type;
	}
	public void setType(DataFileType type) {
		this.type = type;
	}

	public Set<AID> getLocations() {
		return locations;
	}
	public void setLocations(Set<AID> locations) {
		this.locations = locations;
	}
	public void addLocation(AID location) {
		this.locations.add(location);
	}

	public TaskToSolve getProducer() {
		return producer;
	}
	public void setProducer(TaskToSolve producer) {
		this.producer = producer;
	}
	
}
