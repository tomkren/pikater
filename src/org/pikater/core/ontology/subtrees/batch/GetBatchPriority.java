package org.pikater.core.ontology.subtrees.batch;

import jade.content.AgentAction;

public class GetBatchPriority implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 527891995156984131L;
	
	private int batchID;

	public GetBatchPriority(){
	}
	
	public GetBatchPriority(int batchID){
		this.batchID = batchID;
	}
	
	public int getBatchID() {
		return batchID;
	}
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	
}
