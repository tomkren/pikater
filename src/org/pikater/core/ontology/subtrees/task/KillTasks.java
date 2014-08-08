package org.pikater.core.ontology.subtrees.task;

import jade.content.AgentAction;

public class KillTasks implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4646178801749956786L;
	
	private int batchID;

	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	
}
