package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 14:00
 */
public class DataProcessingComputationNode extends ComputationNode {
    private int numberOfInputs;

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public void setNumberOfInputs(int numberOfInputs) {
        this.numberOfInputs = numberOfInputs;
    }
}
