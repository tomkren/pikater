package org.pikater.core.agents.system.manager.graph.edges;


import org.pikater.core.ontology.subtrees.task.Evaluation;

/**
 * Edge with evaluation of the computation
 * User: Kuba
 * Date: 10.5.2014
 * Time: 13:33
 */
public class ErrorEdge extends EdgeValue  {
    private Evaluation evaluation;
    private int computationId;

    /**
     * Instantiates a new Error edge.
     *
     * @param evaluation Evaluation results
     * @param computationId Id of the computation
     */
    public ErrorEdge(Evaluation evaluation,int computationId) {
        this.evaluation = evaluation;
        this.computationId = computationId;
    }

    /**
     * Gets evaluation results
     * @return Evaluation results
     */
    public Evaluation getEvaluation() {
        return evaluation;
    }

    /**
     * Sets evaluation.
     *
     * @param evaluation the evaluation
     */
    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * Gets computation id
     * @return Computation ID
     */
    public int getComputationId() {
        return computationId;
    }
}
