package org.pikater.core.agents.system.computationDescriptionParser.edges;


import org.pikater.core.ontology.subtrees.task.Evaluation;

/**
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge extends EdgeValue {
    private Evaluation evaluation;

    public ErrorEdge(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
