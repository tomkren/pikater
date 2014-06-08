package org.pikater.core.agents.system;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ProblemGraph;

public class ComputationCollectionItem {
	private ProblemGraph problemGraph;
	private ACLMessage message;
	
	public ProblemGraph getProblemGraph() {
		return problemGraph;
	}
	public void setProblemGraph(ProblemGraph problemGraph) {
		this.problemGraph = problemGraph;
	}
	
	public ACLMessage getMessage() {
		return message;
	}
	public void setMessage(ACLMessage message) {
		this.message = message;
	}		
}
