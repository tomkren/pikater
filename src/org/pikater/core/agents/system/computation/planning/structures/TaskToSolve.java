package org.pikater.core.agents.system.computation.planning.structures;

import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

public class TaskToSolve {

	private Task task;
	private Action action;
	private ACLMessage msg;
	private int priority;
	private boolean sendResultToManager;

	public TaskToSolve(Task task, Action action, ACLMessage msg) {
		this.task = task;
		this.action = action;
		this.msg = msg;
		this.priority = task.getPriority();
	}

	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	public Action getAction() {
		return action;
	}

	public ACLMessage getMsg() {
		return msg;
	}

	public int getPriority() {
		return this.priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setDownPriority() {
		this.priority = this.priority -1;
	}

	public boolean isSendResultToManager() {
		return sendResultToManager;
	}
	public void setSendResultToManager(boolean sendResultToManager) {
		this.sendResultToManager = sendResultToManager;
	}
	
}
