package org.pikater.core.ontology.subtrees.experiment;

import org.pikater.core.ontology.subtrees.messages.Problem;

import jade.content.Concept;

public class Solve implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1749701979992449877L;
	private Problem problem;

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
	}
}
