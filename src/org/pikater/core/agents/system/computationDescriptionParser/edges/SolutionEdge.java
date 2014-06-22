package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.subtrees.search.SearchSolution;
import java.util.List;

/**
 * User: Klara
 * Date: 22.6.2014
 * Time: 11:12
 */
public class SolutionEdge extends EdgeValue {
    private List<SearchSolution> options;

    public List<SearchSolution> getOptions() {
        return options;
    }

    public void setOptions(List<SearchSolution> options) {
        this.options = options;
    }
}
