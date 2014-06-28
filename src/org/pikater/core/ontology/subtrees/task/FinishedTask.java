package org.pikater.core.ontology.subtrees.task;

import jade.content.Concept;

public class FinishedTask implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1147257054671778027L;

	private int taskID;
	private int cpuCoreID;

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public int getCpuCoreID() {
		return cpuCoreID;
	}

	public void setCpuCoreID(int cpuCoreID) {
		this.cpuCoreID = cpuCoreID;
	}

}
