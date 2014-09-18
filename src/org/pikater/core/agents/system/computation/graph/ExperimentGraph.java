package org.pikater.core.agents.system.computation.graph;

import java.util.ArrayList;

public class ExperimentGraph {
	protected Integer id;
	
	/*
	 * Top layer of problem tree
	 */
	private ArrayList<ExperimentItem> problems =
			new ArrayList<ExperimentItem>();


	public ArrayList<ExperimentItem> getProblems() {
		return problems;
	}
	public void setProblems(ArrayList<ExperimentItem> problems) {
		this.problems = problems;
	}
	public void addRootProblem(ExperimentItem problem) {
		this.problems.add(problem);
	}
	
	public int getProblemsCount() {
		return this.problems.size();
	}
	
	// Test if all problemItems in dependency graph are finished
	public boolean areAllProblemsFinished() {
		
		// if exists some rootItem which is not finished
		for (ExperimentItem problemI : problems) {
			
			if (problemI.getStatus() != ExperimentItem.ProblemStatus.IS_FINISHED) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<ExperimentItem> getAllIndependentWaitingProblems() {
		
		ArrayList<ExperimentItem> result =
				new ArrayList<ExperimentItem>();
		
		for (ExperimentItem problemI : result) {
			
			ArrayList<ExperimentItem> problemsI =
					problemI.getIndependentItems();
			
			result.addAll(problemsI);
		}
		
		return result;
	}

}
