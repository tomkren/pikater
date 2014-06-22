package org.pikater.core.ontology.subtrees.agentInfo;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class AgentInfos implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4635414687017805968L;

	private List<AgentInfo> agentInfos;

	public List<AgentInfo> getAgentInfos() {
		if (this.agentInfos == null) {
			return new ArrayList<AgentInfo>();
		}
		return agentInfos;
	}

	public void setAgentInfos(List<AgentInfo> agentInfos) {
		this.agentInfos = agentInfos;
	}

	public void addAgentInfo(AgentInfo agentInfo) {
		if (this.agentInfos == null) {
			this.agentInfos = new ArrayList<AgentInfo>();
		}
		this.agentInfos.add(agentInfo);
	}

}
