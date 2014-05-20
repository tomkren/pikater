package org.pikater.core.ontology.subtrees.batch;

import jade.content.Concept;

public class NewBatch implements Concept {

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
