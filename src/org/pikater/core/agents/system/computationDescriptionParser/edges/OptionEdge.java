package org.pikater.core.agents.system.computationDescriptionParser.edges;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.subtrees.option.Option;

import java.util.List;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:28
 */
public class OptionEdge extends EdgeValue {
    private List<Option> options;

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
