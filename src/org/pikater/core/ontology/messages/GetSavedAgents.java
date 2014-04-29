package org.pikater.core.ontology.messages;

import jade.content.AgentAction;

public class GetSavedAgents implements AgentAction {

	private static final long serialVersionUID = 3809082629852138041L;

	private int userID;

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

}
