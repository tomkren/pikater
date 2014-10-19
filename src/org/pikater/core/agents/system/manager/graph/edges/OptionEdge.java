package org.pikater.core.agents.system.manager.graph.edges;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

/**
 * Edge with option list
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:28
 */
public class OptionEdge extends EdgeValue {
    private List<NewOption> options;

    /**
     * Gets option list
     * @return Option list
     */
    public List<NewOption> getOptions() {
        return options;
    }

    /**
     * Set option list
     * @param options Option list
     */
    public void setOptions(List<NewOption> options) {
        this.options = options;
    }
}
