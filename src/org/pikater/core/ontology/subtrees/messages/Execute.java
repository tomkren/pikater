package org.pikater.core.ontology.subtrees.messages;

import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.AgentAction;

public class Execute implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170953913186078035L;
	private Task _task;
	private Agent _method;

	public void setTask(Task task) {
		_task = task;
	}

	public Task getTask() {
		return _task;
	}

	public Agent getMethod() {
		return _method;
	}

	public void setMethod(Agent _method) {
		this._method = _method;
	}

}