package org.pikater.core.ontology.messages;

import jade.content.AgentAction;
import jade.content.onto.basic.Action;

public class GetData implements AgentAction {
	private static final long serialVersionUID = -8760296402786723483L;
	private String file_name;
	private String o2a_agent; // if the field is set, use o2a for DataInstances transfer (requesting agent must be local)

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String fileName) {
		file_name = fileName;
	}

	public String getO2a_agent() {
		return o2a_agent;
	}

	public void setO2a_agent(String o2a_agent) {
		this.o2a_agent = o2a_agent;
	}
}