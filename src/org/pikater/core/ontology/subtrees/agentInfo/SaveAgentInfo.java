package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.AgentAction;

public class SaveAgentInfo implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3690761112095161077L;

	private AgentInfo agentInfo;
	private int userID;

	public AgentInfo getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
