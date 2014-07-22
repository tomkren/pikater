package org.pikater.core.agents.system.computationDescriptionParser.edges;


import org.pikater.core.ontology.subtrees.task.Evaluation;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge extends EdgeValue {
    private Evaluation evaluation;
    private String conversationId;

    public ErrorEdge(Evaluation evaluation,String conversationId) {
        this.evaluation = evaluation;
        this.conversationId = conversationId;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId)
    {
        this.conversationId=conversationId;
    }
}
