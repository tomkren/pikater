package org.pikater.core.agents.system.duration;

import jade.core.AID;

public class BusyAgent {
	
	private AID aid;
	private String taskID;
	
	public BusyAgent(AID aid, String taskID){
		this.aid = aid;
		this.taskID = taskID;
	}

	public AID getAid() {
		return aid;
	}

	public void setAid(AID aid) {
		this.aid = aid;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
}
