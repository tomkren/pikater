package org.pikater.core.agents.system.manager;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;

public class ComputationCollectionItem {
	private ComputationGraph problemGraph;
	private ACLMessage message;
    private int batchID;

    public ComputationCollectionItem(ComputationGraph problemGraph,
    		ACLMessage message, int batchId) {
        this.problemGraph = problemGraph;
        this.message=message;
        this.batchID = batchId;
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

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }
}
