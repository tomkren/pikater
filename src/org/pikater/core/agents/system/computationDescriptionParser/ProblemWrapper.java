package org.pikater.core.agents.system.computationDescriptionParser;

import org.pikater.core.ontology.messages.Problem;

public class ProblemWrapper extends ItemOfGraph {

	private Problem problem = null;
	private String outputFile = null;

	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

}
