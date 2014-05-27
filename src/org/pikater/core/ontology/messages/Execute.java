package org.pikater.core.ontology.messages;

import jade.content.AgentAction;

public class Execute implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7170953913186078035L;
	private Task _task;
	private Agent _search;

	public void setTask(Task task) {
		_task = task;
	}

	public Task getTask() {
		return _task;
	}

	public Agent get_search() {
		return _search;
	}

	public void set_search(Agent _search) {
		this._search = _search;
	}

}