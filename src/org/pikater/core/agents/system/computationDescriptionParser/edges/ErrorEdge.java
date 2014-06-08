package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.subtrees.task.Eval;

import java.util.ArrayList;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge extends EdgeValue {
    private ArrayList<Eval> errors=new ArrayList<>();

    public ArrayList<Eval> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Eval> errors) {
        this.errors = errors;
    }
}
