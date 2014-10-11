package org.pikater.core.ontology.subtrees.batch;

import jade.content.AgentAction;

public class UpdateBatchStatus implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5926949980911534765L;

	private int batchID;
	private String status;
	
	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
