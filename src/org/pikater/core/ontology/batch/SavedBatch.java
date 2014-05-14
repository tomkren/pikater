package org.pikater.core.ontology.batch;

import jade.content.AgentAction;

public class SavedBatch implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5259015542502794903L;

	private int savedBatchId;
	private String message;

	public int getSavedBatchId() {
		return savedBatchId;
	}
	public void setSavedBatchId(int savedBatchId) {
		this.savedBatchId = savedBatchId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
