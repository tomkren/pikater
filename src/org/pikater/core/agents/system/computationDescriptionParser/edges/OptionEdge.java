package org.pikater.core.agents.system.computationDescriptionParser.edges;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.subtrees.option.Option;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:28
 */
public class OptionEdge extends EdgeValue {
    private ArrayList options;

    public ArrayList getOptions() {
        return options;
    }

    public void setOptions(ArrayList options) {
        this.options = options;
    }
}
