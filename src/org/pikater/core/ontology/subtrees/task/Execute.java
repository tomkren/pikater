package org.pikater.core.ontology.subtrees.task;

import org.pikater.core.ontology.subtrees.management.Agent;

import jade.content.AgentAction;

public class Execute implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170953913186078035L;
	private Task task;
	private Agent method;

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}

	public Agent getMethod() {
		return method;
	}
	public void setMethod(Agent method) {
		this.method = method;
	}

}