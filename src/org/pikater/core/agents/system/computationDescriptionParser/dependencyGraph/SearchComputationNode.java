package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:34
 */
public class SearchComputationNode extends ComputationNode {
    private String modelClass;
    
	public SearchComputationNode(StartComputationStrategy executeStrategy) {
        super(executeStrategy);
    }

    public SearchComputationNode() {
        super();
    }
    
    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }
}
