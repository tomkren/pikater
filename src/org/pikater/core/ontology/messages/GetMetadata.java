package org.pikater.core.ontology.messages;

import jade.content.AgentAction;

public class GetMetadata implements AgentAction {

	private static final long serialVersionUID = -8760296402786723483L;
	
	private String external_filename;    
	private String internal_filename;
	private String hash;
	
	@Deprecated
	public String getExternal_filename() {
		return external_filename;
	}
	@Deprecated
	public void setExternal_filename(String external_filename) {
		this.external_filename = external_filename;
	}
	@Deprecated
	public String getInternal_filename() {
		return internal_filename;
	}
	@Deprecated
	public void setInternal_filename(String internal_filename) {
		this.internal_filename = internal_filename;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

}