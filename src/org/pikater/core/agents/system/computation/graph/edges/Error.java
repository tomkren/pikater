package org.pikater.core.agents.system.computation.graph.edges;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:34
 */
public class Error {
    private String name;
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
