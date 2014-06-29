package org.pikater.core.agents.system.planner;

import org.pikater.core.ontology.subtrees.task.Task;

import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

public class TaskToSolve {

	private Task task;
	private Action action;
	private ACLMessage msg;

	public TaskToSolve(Task task, Action action, ACLMessage msg) {
		this.task = task;
		this.action = action;
		this.msg = msg;
	}

	public Task getTask() {
		return task;
	}

	public Action getAction() {
		return action;
	}

	public ACLMessage getMsg() {
		return msg;
	}

}
