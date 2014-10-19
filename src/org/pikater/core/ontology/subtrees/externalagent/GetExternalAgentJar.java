package org.pikater.core.ontology.subtrees.externalagent;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

public class GetExternalAgentJar implements AgentAction {

	private static final long serialVersionUID = 8319731892913650189L;

	private String type;

	@Slot(mandatory = true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
