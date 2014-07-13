package org.pikater.core.agents.system.computationDescriptionParser.edges;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:28
 */
public class OptionEdge extends EdgeValue {
    private List<NewOption> options;

    public List<NewOption> getOptions() {
        return options;
    }

    public void setOptions(List<NewOption> options) {
        this.options = options;
    }
}
