package org.pikater.core.ontology.subtrees.management;

import org.pikater.core.ontology.subtrees.task.Id;

import jade.content.AgentAction;

public class GetAgents implements AgentAction {

	private static final long serialVersionUID = -3423064460137052507L;
	private Agent agent;
	private int number;
	private Id taskID;

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Id getTaskID() {
		return taskID;
	}

	public void setTaskID(Id taskID) {
		this.taskID = taskID;
	}
}