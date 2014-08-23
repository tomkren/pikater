package org.pikater.core.ontology.subtrees.agent;

import jade.content.AgentAction;

public class AgentClass  implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5822829622281554852L;
	
	private String agentClass;
	private int userID;

	public AgentClass() {}
	
	public AgentClass(String agentClass, int userID) {
		this.agentClass = agentClass;
		this.userID = userID;
	}
	
	public String getAgentClass() {
		return agentClass;
	}
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
