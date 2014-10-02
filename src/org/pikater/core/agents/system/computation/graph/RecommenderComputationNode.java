package org.pikater.core.agents.system.computation.graph;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:27
 */
public class RecommenderComputationNode extends ComputationNode {
    private String recommenderClass;

    public RecommenderComputationNode(StartComputationStrategy executeStrategy,ComputationGraph computationGraph) {
        super(executeStrategy);
    }

    public RecommenderComputationNode(ComputationGraph computationGraph) {
        super(computationGraph);
    }

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }
}
