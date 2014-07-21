package org.pikater.core.ontology.subtrees.search;

import jade.content.onto.basic.Action;

import java.util.List;

public class ExecuteParameters extends Action{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 599262534378620154L;

	private List<SearchSolution> solutions; // list of lists of Options

	public List<SearchSolution> getSolutions() {
		return solutions;
	}
	public void setSolutions(List<SearchSolution> solutions) {
		this.solutions = solutions;
	}

}
