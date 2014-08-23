package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.AgentAction;

public class GetAgentInfos implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5582405682331716949L;

	private int userID;

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
