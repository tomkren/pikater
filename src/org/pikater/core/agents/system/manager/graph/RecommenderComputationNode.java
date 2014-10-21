package org.pikater.core.agents.system.manager.graph;

/**
 * Computation node for recommending User: Kuba Date: 18.5.2014 Time: 15:27
 */
public class RecommenderComputationNode extends ComputationNode {
	private String recommenderClass;

	/**
	 * Instantiates a new Recommender computation node.
	 * 
	 * @param executeStrategy
	 *            the execute strategy
	 */
	public RecommenderComputationNode(StartComputationStrategy executeStrategy) {
		super(executeStrategy);
	}

	/**
	 * Instantiates a new Recommender computation node.
	 * 
	 * @param computationGraph
	 *            the computation graph
	 */
	public RecommenderComputationNode(ComputationGraph computationGraph) {
		super(computationGraph);
	}

	/**
	 * Gets recommender class.
	 * 
	 * @return the recommender class
	 */
	public String getRecommenderClass() {
		return recommenderClass;
	}

	/**
	 * Sets recommender class.
	 * 
	 * @param recommenderClass
	 *            the recommender class
	 */
	public void setRecommenderClass(String recommenderClass) {
		this.recommenderClass = recommenderClass;
	}
}
