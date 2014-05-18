package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.messages.Agent;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:36
 */
public class RecommendationEdge extends EdgeValue {
    private Agent recommendation;

    public Agent getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(Agent recommendation) {
		this.recommendation = recommendation;
	}
	public RecommendationEdge(Agent recommendation) {
        this.recommendation = recommendation;
    }
}
