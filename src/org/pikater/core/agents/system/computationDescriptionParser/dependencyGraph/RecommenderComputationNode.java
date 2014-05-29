package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:27
 */
public class RecommenderComputationNode extends ComputationNode {
    private String recommenderClass;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }
}
