package org.pikater.core.ontology.subtrees.batch;

import jade.content.AgentAction;

public class LoadBatch implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3741609123516110146L;

	private int batchID;

	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

}
