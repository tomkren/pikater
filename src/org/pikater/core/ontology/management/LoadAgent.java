package org.pikater.core.ontology.management;

import org.pikater.core.ontology.messages.Execute;

import jade.content.AgentAction;

public class LoadAgent implements AgentAction {

	private static final long serialVersionUID = -2890249253440084L;

	private String filename;
	private Execute first_action = null;
	private byte [] object;
	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setFirst_action(Execute first_action) {
		this.first_action = first_action;
	}
	public Execute getFirst_action() {
		return first_action;
	}

	public void setObject(byte [] object) {
		this.object = object;
	}
	public byte [] getObject() {
		return object;
	}
	
}
