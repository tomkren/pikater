package org.pikater.core.ontology.subtrees.task;

import jade.content.AgentAction;

public class ExecuteTask implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170953913186078035L;
	private Task task;

	public void setTask(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

}