package org.pikater.core.ontology.subtrees.batch;

import org.pikater.core.ontology.subtrees.batchDescription.ComputationDescription;

import jade.content.AgentAction;

public class ExecuteBatch implements AgentAction {

	private static final long serialVersionUID = 1L;
	private ComputationDescription description;

	public ExecuteBatch(ComputationDescription description) {
		this.description = description;
	}

	public ExecuteBatch() {
	}

	public ComputationDescription getDescription() {
		return description;
	}

	public void setDescription(ComputationDescription description) {
		this.description = description;
	}
	
}
