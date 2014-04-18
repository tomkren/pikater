package org.pikater.core.agents.system.computationDescriptionParser;

import java.util.ArrayList;

import org.pikater.core.agents.system.computationDescriptionParser.items.ProblemWrapper;

public class OntologyGraph {

	/*
	 * Top layer of problem tree
	 */
	private ArrayList<ProblemWrapper> problems =
			new ArrayList<ProblemWrapper>();


	public ArrayList<ProblemWrapper> getProblems() {
		return problems;
	}
	public void setProblems(ArrayList<ProblemWrapper> problems) {
		this.problems = problems;
	}
	public void addRootProblem(ProblemWrapper problem) {
		this.problems.add(problem);
	}
	
}
