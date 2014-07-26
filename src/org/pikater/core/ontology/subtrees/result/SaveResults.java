package org.pikater.core.ontology.subtrees.result;

import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.AgentAction;

public class SaveResults implements AgentAction {

	private static final long serialVersionUID = -7028457864866356063L;

	private Task task;
	private int experimentID;

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}

	public int getExperimentID() {
		return experimentID;
	}
	public void setExperimentID(int experimentID) {
		this.experimentID = experimentID;
	}

}