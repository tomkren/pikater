package org.pikater.core.ontology.subtrees.agentInfo;

import java.util.ArrayList;
import java.util.List;

public class ExternalAgentNames {

	private List<String> agentNames;
	
	@Deprecated
	public ExternalAgentNames() {
		this.agentNames = new ArrayList<String>();
	}

	public ExternalAgentNames(List<String> agentNames) {
		this.agentNames = agentNames;
	}

	public List<String> getAgentNames() {
		return agentNames;
	}
	public void setAgentNames(List<String> agentNames) {
		this.agentNames = agentNames;
	}
	
}
