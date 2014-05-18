package org.pikater.core.ontology.subtrees.data;

import jade.content.AgentAction;

public class GetData implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4329373882355841387L;

	private String fileName;
	private String o2aAgent; // if the field is set, use o2a for DataInstances transfer (requesting agent must be local)

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getO2aAgent() {
		return o2aAgent;
	}
	public void setO2aAgent(String o2aAgent) {
		this.o2aAgent = o2aAgent;
	}

}