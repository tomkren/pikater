package org.pikater.core.agents.system.manager.graph.edges;

import org.pikater.core.ontology.subtrees.search.SearchSolution;

/**
 * Edge for searching
 * User: Klara
 * Date: 22.6.2014
 * Time: 11:12
 */
public class SolutionEdge extends EdgeValue {
    private SearchSolution options;
    private int computationID;

    /**
     * Gets options to be searched
     * @return Options to be searched
     */
    public SearchSolution getOptions() {
        return options;
    }

    /**
     * Sets options to be searched
     * @param options Options to be seacrhed
     */
    public void setOptions(SearchSolution options) {
        this.options = options;
    }

    /**
     * Gets computation id
     * @return Computation ID
     */
	public int getComputationID() {
		return computationID;
	}

    /**
     * Sets computation id
     * @param computationID Computation ID
     */
	public void setComputationID(int computationID) {
		this.computationID = computationID;
	}
}
