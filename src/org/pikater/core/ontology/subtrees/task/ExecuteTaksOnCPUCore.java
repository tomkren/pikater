package org.pikater.core.ontology.subtrees.task;

import jade.content.AgentAction;

public class ExecuteTaksOnCPUCore implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -692225931446356019L;

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
