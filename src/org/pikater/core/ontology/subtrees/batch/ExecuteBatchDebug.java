package org.pikater.core.ontology.subtrees.batch;

import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;

public class ExecuteBatchDebug {

	private ComputationDescription description;
	private int batchID;
	
	@Deprecated
	public ExecuteBatchDebug() {}

	public ExecuteBatchDebug(
			ComputationDescription description, int batchID) {
		this.description = description;
		this.batchID = batchID;
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
