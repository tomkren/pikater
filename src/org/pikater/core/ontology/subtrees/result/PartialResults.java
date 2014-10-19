package org.pikater.core.ontology.subtrees.result;

import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.Predicate;
import jade.util.leap.List;

public class PartialResults implements Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -707946881612003753L;
	// sent only with the first results
	private Task task;
	private String taskID;
	private List results;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}
}