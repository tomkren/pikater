package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

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
	
	// Test if all problemItems in dependency graph are finished
	public boolean areAllProblemsFinished() {
		
		// if exists some rootItem which is not finished
		for (ProblemItem problemI : problems) {
			
			if (problemI.getStatus() != ProblemItem.ProblemStatus.IS_FINISHED) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<ProblemItem> getAllIndependetWaitingProblems() {
		
		ArrayList<ProblemItem> problems =
				new ArrayList<ProblemItem>();
		
		for (ProblemItem problemI : problems) {
			
			ArrayList<ProblemItem> problemsI =
					problemI.getIndependentItems();
			
			problems.addAll(problemsI);
		}
		
		return problems;
	}

}
