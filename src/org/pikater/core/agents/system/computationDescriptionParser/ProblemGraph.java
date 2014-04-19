package org.pikater.core.agents.system.computationDescriptionParser;

import java.util.ArrayList;

public class ProblemGraph {

	/*
	 * Top layer of problem tree
	 */
	private ArrayList<ProblemItem> problems =
			new ArrayList<ProblemItem>();


	public ArrayList<ProblemItem> getProblems() {
		return problems;
	}
	public void setProblems(ArrayList<ProblemItem> problems) {
		this.problems = problems;
	}
	public void addRootProblem(ProblemItem problem) {
		this.problems.add(problem);
	}
	
	public int getNumOfProblems() {
		return this.problems.size();
	}
}
