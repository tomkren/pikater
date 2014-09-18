package org.pikater.core.agents.system.computation.graph;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.experiment.Experiment;

public class ExperimentItem {

	private Experiment problem = null;
	private ProblemStatus status = null;
	private String outputFile = null;
	
	private List<ExperimentItem> dependentSons = new ArrayList<ExperimentItem>();

	public static enum ProblemStatus {
		IS_WAITING,    // is waiting in queue to start computing
		IS_COMPUTING,  // now is problem computing
		IS_FINISHED    // problem was computed and now is finished
	}
	
	public Experiment getProblem() {
		return problem;
	}
	public void setProblem(Experiment problem) {
		this.problem = problem;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public List<ExperimentItem> getDependentSons() {
		return dependentSons;
	}
	public void setDependentSons(List<ExperimentItem> dependentSons) {
		this.dependentSons = dependentSons;
	}
	public void getDependentSons(ExperimentItem problem) {
		this.dependentSons.add(problem);
	}

	public ProblemStatus getStatus() {
		return status;
	}
	public void setStatus(ProblemStatus status) {
		this.status = status;
	}

	public List<ExperimentItem> getIndependentItems() {

		// If this Item is not waiting in queue, than his sons are not waiting to
		if ( (this.getStatus() == ExperimentItem.ProblemStatus.IS_COMPUTING) ||
			 (this.getStatus() == ExperimentItem.ProblemStatus.IS_FINISHED) ) {

			return new ArrayList<ExperimentItem>();
		}
		
		List<ExperimentItem> problems =
				new ArrayList<ExperimentItem>();

		
		int numOfIndependentSons = 0;

		for (ExperimentItem problem : dependentSons) {
			if (problem.getStatus() == ExperimentItem.ProblemStatus.IS_FINISHED) {
				numOfIndependentSons++;
			}
		}
		
		if (dependentSons.size() == numOfIndependentSons) {
			problems.add(this);
			return problems;
		}
		

		// if this item is waiting and some his soon is waiting to - recursion
		for (ExperimentItem problem : dependentSons) {
			
			List<ExperimentItem> problemsI =
					problem.getDependentSons();
			
			problems.addAll(problemsI);
		}

		return problems;
	}
}
