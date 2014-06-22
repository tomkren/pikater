package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:34
 */
public class SearchComputationNode extends ComputationNode {
    private String modelClass;
    private String defaultPackagePrefix="org.pikater.core.agents.experiment.search.";

    
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
        if (!modelClass.contains("."))
        {
            modelClass=defaultPackagePrefix+modelClass;
        }
        this.modelClass = modelClass;
    }
}
