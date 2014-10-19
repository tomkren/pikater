package org.pikater.core.agents.system.manager.graph.edges;

/**
 * Key value pair - erorr - error rate
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:34
 */
public class Error {
    private String name;
    private Double value;

    /**
     * Gets value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
}
