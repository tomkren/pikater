package org.pikater.core.ontology.subtrees.task;

import jade.content.Concept;

public class FinishedTask implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1147257054671778027L;

	private Task task;
	private int cpuCoreID;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getCpuCoreID() {
		return cpuCoreID;
	}

	public void setCpuCoreID(int cpuCoreID) {
		this.cpuCoreID = cpuCoreID;
	}

}
