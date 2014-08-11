package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.agents.experiment.search.Agent_Search;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:34
 */
public class SearchComputationNode extends ComputationNode {
    private String modelClass;
    private String defaultPackagePrefix= Agent_Search.class.getPackage().getName() + ".";

    
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
