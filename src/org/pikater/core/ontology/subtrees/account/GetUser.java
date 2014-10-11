package org.pikater.core.ontology.subtrees.account;

import jade.content.AgentAction;

/**
 * Request for a {@link User} object that contains account details for
 * the user ID specified in this request.
 * 
 * @author stepan
 */
public class GetUser implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4116937510862858239L;

	private int userID;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
