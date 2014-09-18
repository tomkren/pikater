package org.pikater.core.agents.system.computation.graph.edges;

import org.pikater.core.ontology.subtrees.search.SearchSolution;

/**
 * User: Klara
 * Date: 22.6.2014
 * Time: 11:12
 */
public class SolutionEdge extends EdgeValue {
    private SearchSolution options;
    private int computationID;

    public SearchSolution getOptions() {
        return options;
    }

    public void setOptions(SearchSolution options) {
        this.options = options;
    }

	public int getComputationID() {
		return computationID;
	}

	public void setComputationID(int computationID) {
		this.computationID = computationID;
	}
}
