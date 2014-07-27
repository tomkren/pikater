package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.subtrees.management.Agent;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:36
 */
public class AgentTypeEdge extends EdgeValue {
    private String agentType;
    
	public AgentTypeEdge(String agentType) {
        this.agentType = agentType;
    }

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
}
