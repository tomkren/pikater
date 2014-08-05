package org.pikater.core.ontology.subtrees.agentInfo;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.agent.AgentClass;

public class ExternalAgentNames implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3513576942162577469L;
	
	private List<AgentClass> agentNames;
	
	@Deprecated
	public ExternalAgentNames() {
		this.agentNames = new ArrayList<AgentClass>();
	}

	public ExternalAgentNames(List<AgentClass> agentNames) {
		this.agentNames = agentNames;
	}

	public List<AgentClass> getAgentNames() {
		return agentNames;
	}
	public void setAgentNames(List<AgentClass> agentNames) {
		this.agentNames = agentNames;
	}
	
}
