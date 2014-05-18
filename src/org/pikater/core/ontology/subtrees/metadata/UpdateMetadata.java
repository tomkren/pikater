package org.pikater.core.ontology.subtrees.metadata;

import jade.content.AgentAction;

public class UpdateMetadata implements AgentAction {

	private static final long serialVersionUID = 39194673393127712L;
	Metadata metadata;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}