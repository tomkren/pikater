package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.subtrees.task.Eval;

import java.util.ArrayList;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge extends EdgeValue {
    private float value;

    public ErrorEdge(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
