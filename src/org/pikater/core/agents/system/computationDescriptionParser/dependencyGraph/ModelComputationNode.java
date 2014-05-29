package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:52
 */
public class ModelComputationNode extends ComputationNode {
    private String modelClass;

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }
}
