package org.pikater.core.ontology.subtrees.batch;

import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;

public class ExecuteBatchDebug {

	private ComputationDescription description;
	private int batchID;
	private int userID;
	
	@Deprecated
	public ExecuteBatchDebug() {}

	public ExecuteBatchDebug(
			ComputationDescription description, int batchID, int userID) {
		this.description = description;
		this.batchID = batchID;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public ComputationDescription getDescription() {
		return description;
	}
	public void setDescription(ComputationDescription description) {
		this.description = description;
	}

	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

}
