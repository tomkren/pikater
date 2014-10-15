package org.pikater.core.ontology.subtrees.account;

import org.pikater.core.agents.system.Agent_DataManager;

import jade.content.AgentAction;

/**
 * A message sent to {@link Agent_DataManager} which should respond
 * with the requested user ID.
 * 
 * @author stepan
 */
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
