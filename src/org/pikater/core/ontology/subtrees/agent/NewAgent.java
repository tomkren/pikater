package org.pikater.core.ontology.subtrees.agent;

import jade.content.AgentAction;

public class NewAgent implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4419619980477618731L;
	private String agentClassName;
	private int userID;
	
	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
