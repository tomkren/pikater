package org.pikater.core.ontology.subtrees.metadata;

import jade.content.AgentAction;

public class GetMetadata implements AgentAction {

	private static final long serialVersionUID = -8760296402786723483L;

	private String externalFilename;
	private String internalFilename;
	private String hash;

	// Deprecated
	public String getExternalFilename() {
		return externalFilename;
	}

	// Deprecated
	public void setExternalFilename(String externalFilename) {
		this.externalFilename = externalFilename;
	}

	// Deprecated
	public String getInternalFilename() {
		return internalFilename;
	}

	// Deprecated
	public void setInternalFilename(String internalFilename) {
		this.internalFilename = internalFilename;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}