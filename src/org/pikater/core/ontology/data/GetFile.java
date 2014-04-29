package org.pikater.core.ontology.data;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

public class GetFile implements AgentAction {

	private static final long serialVersionUID = 4335808324680385414L;

	private String hash;

	@Slot(mandatory = true)
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
