package org.pikater.core.ontology.subtrees.metadata;

import jade.content.AgentAction;

public class NewComputedData implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 246978375631905288L;

	private int computedDataID;
	private int userID;
	
	
	public int getComputedDataID() {
		return computedDataID;
	}
	public void setComputedDataID(int computedDataID) {
		this.computedDataID = computedDataID;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userId) {
		this.userID = userId;
	}
	
}
