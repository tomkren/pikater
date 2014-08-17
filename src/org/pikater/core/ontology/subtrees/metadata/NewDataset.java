package org.pikater.core.ontology.subtrees.metadata;

import jade.content.AgentAction;


public class NewDataset implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014780512687910546L;

	private int dataSetID;
	private int userID;

	
	public int getDataSetID() {
		return dataSetID;
	}
	public void setDataSetID(int dataSetID) {
		this.dataSetID = dataSetID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}

}
