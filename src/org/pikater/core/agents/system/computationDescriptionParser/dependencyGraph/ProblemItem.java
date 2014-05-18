package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.oldPikaterMessages.Problem;

public class ProblemItem {

	private Problem problem = null;
	private ProblemStatus status = null;
	private String outputFile = null;
	
	private ArrayList<ProblemItem> dependentSons =
			new ArrayList<ProblemItem>();

	public static enum ProblemStatus {
		IS_WAITING,    // is waiting in queue to start computing
		IS_COMPUTING,  // now is problem computing
		IS_FINISHED    // problem was computed and now is finished
	}
	
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

	public ArrayList<ProblemItem> getDependentSons() {
		return dependentSons;
	}
	public void setDependentSons(ArrayList<ProblemItem> dependentSons) {
		this.dependentSons = dependentSons;
	}
	public void getDependentSons(ProblemItem problem) {
		this.dependentSons.add(problem);
	}

	public ProblemStatus getStatus() {
		return status;
	}
	public void setStatus(ProblemStatus status) {
		this.status = status;
	}

	public ArrayList<ProblemItem> getIndependentItems() {

		// If this Item is not waiting in queue, than his sons are not waiting to
		if ( (this.getStatus() == ProblemItem.ProblemStatus.IS_COMPUTING) ||
			 (this.getStatus() == ProblemItem.ProblemStatus.IS_FINISHED) ) {

			return new ArrayList<ProblemItem>();
		}
		
		ArrayList<ProblemItem> problems =
				new ArrayList<ProblemItem>();

		
		int numOfIndependentSons = 0;

		for (ProblemItem problem : dependentSons) {
			if (problem.getStatus() == ProblemItem.ProblemStatus.IS_FINISHED) {
				numOfIndependentSons++;
			}
		}
		
		if (dependentSons.size() == numOfIndependentSons) {
			problems.add(this);
			return problems;
		}
		

		// if this item is waiting and some his soon is waiting to - recursion
		for (ProblemItem problem : dependentSons) {
			
			ArrayList<ProblemItem> problemsI =
					problem.getDependentSons();
			
			problems.addAll(problemsI);
		}

		return problems;
	}
}
