package org.pikater.core.ontology.management;

import org.pikater.core.ontology.messages.Agent;

import jade.content.AgentAction;

public class SaveAgent implements AgentAction {

	private static final long serialVersionUID = -2890249253440084L;

	private int userID = 1;
	private Agent agent;
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}	

}