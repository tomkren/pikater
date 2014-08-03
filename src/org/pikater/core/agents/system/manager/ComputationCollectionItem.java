package org.pikater.core.agents.system.manager;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;

public class ComputationCollectionItem {
	private ComputationGraph problemGraph;
	private ACLMessage message;
    private int computationId;

    public ComputationCollectionItem(ComputationGraph problemGraph, ACLMessage message) {
        this.problemGraph=problemGraph;
        this.message=message;
    }

    public ComputationGraph getProblemGraph() {
		return problemGraph;
	}
	public void setProblemGraph(ComputationGraph problemGraph) {
		this.problemGraph = problemGraph;
	}
	
	public ACLMessage getMessage() {
		return message;
	}
	public void setMessage(ACLMessage message) {
		this.message = message;
	}

    public int getComputationId() {
        return computationId;
    }

    public void setComputationId(int computationId) {
        this.computationId = computationId;
    }
}
