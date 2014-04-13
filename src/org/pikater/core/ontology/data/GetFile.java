package org.pikater.core.ontology.data;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

public class GetFile implements AgentAction {
	private String hash;

	@Slot(mandatory = true)
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
