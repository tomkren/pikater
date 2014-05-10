package org.pikater.core.agents.system.computationDescriptionParser.edges;

import java.util.ArrayList;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge {
    private ArrayList<Error> errors=new ArrayList<>();

    public ArrayList<Error> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Error> errors) {
        this.errors = errors;
    }
}
