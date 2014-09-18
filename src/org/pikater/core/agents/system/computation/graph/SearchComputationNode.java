package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.experiment.search.Agent_Search;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:34
 */
public class SearchComputationNode extends ComputationNode {
    private String modelClass;
    private String defaultPackagePrefix= Agent_Search.class.getPackage().getName() + ".";

    
	public SearchComputationNode(StartComputationStrategy executeStrategy,ComputationGraph computationGraph) {
        super(executeStrategy,computationGraph);
    }

    public SearchComputationNode(ComputationGraph computationGraph) {
        super(computationGraph);
    }
    
    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String newModelClass)
    {
        this.modelClass = !newModelClass.contains(".") ? defaultPackagePrefix + newModelClass : newModelClass;
    }
}