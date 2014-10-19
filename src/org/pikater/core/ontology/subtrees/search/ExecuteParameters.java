package org.pikater.core.ontology.subtrees.search;

import jade.content.AgentAction;

import java.util.List;

public class ExecuteParameters implements AgentAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 599262534378620154L;

	// list of lists of Options
	private List<SearchSolution> solutions;

	public List<SearchSolution> getSolutions() {
		return solutions;
	}
	public void setSolutions(List<SearchSolution> solutions) {
		this.solutions = solutions;
	}

}
