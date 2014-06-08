package org.pikater.core.agents.system;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationGraph;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemGraph;

public class ComputationCollectionItem {
	private ComputationGraph problemGraph;
	private ACLMessage message;

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
}
