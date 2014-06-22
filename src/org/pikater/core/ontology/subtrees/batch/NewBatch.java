package org.pikater.core.ontology.subtrees.batch;

import jade.content.AgentAction;


public class NewBatch implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6249718135778390294L;

	private int batchId;
	private int userId;

	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

}
