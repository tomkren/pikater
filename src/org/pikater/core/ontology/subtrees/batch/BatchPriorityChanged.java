package org.pikater.core.ontology.subtrees.batch;

import jade.content.AgentAction;

public class BatchPriorityChanged implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4163169737719616361L;
	
	private int batchID;

	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	
}
