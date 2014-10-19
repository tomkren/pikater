package org.pikater.core.ontology.subtrees.agentinfo;

import jade.content.AgentAction;

public class GetAgentInfoVisibleForUser implements AgentAction {

	private static final long serialVersionUID = -9018024161636538318L;
	
	private int userID;

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
