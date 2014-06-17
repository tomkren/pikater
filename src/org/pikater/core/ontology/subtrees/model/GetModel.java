package org.pikater.core.ontology.subtrees.model;

import jade.content.AgentAction;

public class GetModel implements AgentAction {
	private static final long serialVersionUID = -6670183431289089024L;
	private int modelID;

	public GetModel(){}
	
	public GetModel(int modelID) {
		super();
		this.modelID = modelID;
	}

	public int getModelID() {
		return modelID;
	}

	public void setModelID(int modelID) {
		this.modelID = modelID;
	}
	
}
