package org.pikater.core.ontology.subtrees.messages;

import jade.content.AgentAction;

public class GetFiles implements AgentAction {

	private static final long serialVersionUID = -2890249253440086934L;

	private int userID;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}