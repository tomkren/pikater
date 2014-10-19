package org.pikater.core.agents.system.manager.graph;

import org.pikater.core.agents.experiment.search.Agent_Search;

/**
 * Computation node for searching User: Kuba Date: 18.5.2014 Time: 15:34
 */
public class SearchComputationNode extends ComputationNode {
	private String modelClass;
	private String defaultPackagePrefix = Agent_Search.class.getPackage()
			.getName() + ".";

	/**
	 * Instantiates a new Search computation node.
	 * 
	 * @param executeStrategy
	 *            Strategy to execute
	 */
	public SearchComputationNode(StartComputationStrategy executeStrategy) {
		super(executeStrategy);
	}

	/**
	 * Instantiates a new Search computation node.
	 * 
	 * @param computationGraph
	 *            the computation graph
	 */
	public SearchComputationNode(ComputationGraph computationGraph) {
		super(computationGraph);
	}

	/**
	 * Gets model class.
	 * 
	 * @return the model class
	 */
	public String getModelClass() {
		return modelClass;
	}

	/**
	 * Sets model class.
	 * 
	 * @param newModelClass
	 *            the new model class
	 */
	public void setModelClass(String newModelClass) {
		this.modelClass = !newModelClass.contains(".") ? defaultPackagePrefix
				+ newModelClass : newModelClass;
	}
}