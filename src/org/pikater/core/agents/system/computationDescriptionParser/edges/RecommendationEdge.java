package org.pikater.core.agents.system.computationDescriptionParser.edges;

import org.pikater.core.ontology.messages.Agent;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:36
 */
public class RecommendationEdge {
    private Agent recommendation;

    public RecommendationEdge(Agent recommendation) {
        this.recommendation = recommendation;
    }
}
