package org.pikater.core.agents.system.manager;

import jade.lang.acl.ACLMessage;

import org.pikater.core.agents.system.manager.graph.ComputationGraph;

/**
 * The type Computation collection item.
 */
public class ComputationCollectionItem {
	private ComputationGraph problemGraph;
	private ACLMessage message;
    private int batchID;

    /**
     * Instantiates a new Computation collection item.
     *
     * @param problemGraph the problem graph
     * @param message the message
     * @param batchId the batch id
     */
    public ComputationCollectionItem(ComputationGraph problemGraph,
    		ACLMessage message, int batchId) {
        this.problemGraph = problemGraph;
        this.message=message;
        this.batchID = batchId;
    }

    /**
     * Gets problem graph.
     *
     * @return the problem graph
     */
    public ComputationGraph getProblemGraph() {
		return problemGraph;
	}

    /**
     * Sets problem graph.
     *
     * @param problemGraph the problem graph
     */
    public void setProblemGraph(ComputationGraph problemGraph) {
		this.problemGraph = problemGraph;
	}

    /**
     * Gets message.
     *
     * @return the message
     */
    public ACLMessage getMessage() {
		return message;
	}

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(ACLMessage message) {
		this.message = message;
	}

    /**
     * Gets batch iD.
     *
     * @return the batch iD
     */
    public int getBatchID() {
        return batchID;
    }

    /**
     * Sets batch iD.
     *
     * @param batchID the batch iD
     */
    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }
}
