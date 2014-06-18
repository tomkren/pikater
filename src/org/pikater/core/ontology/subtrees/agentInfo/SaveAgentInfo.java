package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.AgentAction;

public class SaveAgentInfo implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3690761112095161077L;

	private AgentInfo agentInfo;

	public AgentInfo getAgentInfo() {
		return agentInfo;
	}

	public void setAgentInfo(AgentInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
	
}
