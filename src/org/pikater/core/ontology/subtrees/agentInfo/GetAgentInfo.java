package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.AgentAction;

/**
 * Request for a {@link AgentInfo} object of the given agent specified
 * by the {@link #agentClassName} field.
 * 
 * @author stepan
 */
public class GetAgentInfo implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3570705124187463742L;

	private String agentClassName;

	public GetAgentInfo() {}
	
	public GetAgentInfo(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	
	public String getAgentClassName() {
		return agentClassName;
	}
	public void setAgentClassName(String agentClassName) {
		this.agentClassName = agentClassName;
	}
	
}
