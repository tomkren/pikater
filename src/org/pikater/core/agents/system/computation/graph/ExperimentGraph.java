package org.pikater.core.agents.system.computation.graph;

import java.util.ArrayList;
import java.util.List;

public class ExperimentGraph {
	protected Integer id;
	
	/*
	 * Top layer of problem tree
	 */
	private List<ExperimentItem> problems = new ArrayList<ExperimentItem>();

	public List<ExperimentItem> getProblems() {
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

	public List<ExperimentItem> getAllIndependentWaitingProblems() {
		
		List<ExperimentItem> result =
				new ArrayList<ExperimentItem>();
		
		for (ExperimentItem problemI : result) {
			
			List<ExperimentItem> problemsI =
					problemI.getIndependentItems();
			
			result.addAll(problemsI);
		}
		
		return result;
	}

}
