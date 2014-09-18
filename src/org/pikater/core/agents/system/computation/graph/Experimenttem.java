package org.pikater.core.agents.system.computation.graph;

import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.experiment.Experiment;

public class Experimenttem {

	private Experiment problem = null;
	private ProblemStatus status = null;
	private String outputFile = null;
	
	private ArrayList<Experimenttem> dependentSons =
			new ArrayList<Experimenttem>();

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

	public ArrayList<Experimenttem> getDependentSons() {
		return dependentSons;
	}
	public void setDependentSons(ArrayList<Experimenttem> dependentSons) {
		this.dependentSons = dependentSons;
	}
	public void getDependentSons(Experimenttem problem) {
		this.dependentSons.add(problem);
	}

	public ProblemStatus getStatus() {
		return status;
	}
	public void setStatus(ProblemStatus status) {
		this.status = status;
	}

	public ArrayList<Experimenttem> getIndependentItems() {

		// If this Item is not waiting in queue, than his sons are not waiting to
		if ( (this.getStatus() == Experimenttem.ProblemStatus.IS_COMPUTING) ||
			 (this.getStatus() == Experimenttem.ProblemStatus.IS_FINISHED) ) {

			return new ArrayList<Experimenttem>();
		}
		
		ArrayList<Experimenttem> problems =
				new ArrayList<Experimenttem>();

		
		int numOfIndependentSons = 0;

		for (Experimenttem problem : dependentSons) {
			if (problem.getStatus() == Experimenttem.ProblemStatus.IS_FINISHED) {
				numOfIndependentSons++;
			}
		}
		
		if (dependentSons.size() == numOfIndependentSons) {
			problems.add(this);
			return problems;
		}
		

		// if this item is waiting and some his soon is waiting to - recursion
		for (Experimenttem problem : dependentSons) {
			
			ArrayList<Experimenttem> problemsI =
					problem.getDependentSons();
			
			problems.addAll(problemsI);
		}

		return problems;
	}
}
