package org.pikater.core.agents.system.computation.graph.events;

/**
 * Batch finished system event
 * User: Kuba
 * Date: 16.8.2014
 * Time: 18:00
 */
public class BatchFinished {
	
    private final int batchID;

    /**
     * Instantiates a new Batch finished.
     *
     * @param batchID the batch iD
     */
    public BatchFinished(int batchID) {

        this.batchID = batchID;
    }

    /**
     * Gets batch iD.
     *
     * @return the batch iD
     */
    public int getBatchID() {
        return batchID;
    }
}
