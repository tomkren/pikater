package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import java.util.ArrayList;

public class ExperimentGraph {
	protected Integer id;
	
	/*
	 * Top layer of problem tree
	 */
	private ArrayList<Experimenttem> problems =
			new ArrayList<Experimenttem>();


	public ArrayList<Experimenttem> getProblems() {
		return problems;
	}
	public void setProblems(ArrayList<Experimenttem> problems) {
		this.problems = problems;
	}
	public void addRootProblem(Experimenttem problem) {
		this.problems.add(problem);
	}
	
	public int getNumOfProblems() {
		return this.problems.size();
	}
	
	// Test if all problemItems in dependency graph are finished
	public boolean areAllProblemsFinished() {
		
		// if exists some rootItem which is not finished
		for (Experimenttem problemI : problems) {
			
			if (problemI.getStatus() != Experimenttem.ProblemStatus.IS_FINISHED) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Experimenttem> getAllIndependetWaitingProblems() {
		
		ArrayList<Experimenttem> problems =
				new ArrayList<Experimenttem>();
		
		for (Experimenttem problemI : problems) {
			
			ArrayList<Experimenttem> problemsI =
					problemI.getIndependentItems();
			
			problems.addAll(problemsI);
		}
		
		return problems;
	}

}
