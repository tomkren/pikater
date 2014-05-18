package org.pikater.core.ontology.subtrees.dataSource;

import jade.content.AgentAction;
import jade.content.onto.annotations.Slot;

/** Asks an agent to open a socket and upload the dataset with the given hash on the first connection */
public class PrepareFileUpload implements AgentAction {

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
