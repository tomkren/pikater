package org.pikater.core.ontology.subtrees.account;

import jade.content.AgentAction;

public class GetUserID implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894041748901580807L;

	private String login;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
