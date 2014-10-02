package org.pikater.core.agents.system.computation.graph.events;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 18:00
 */
public class BatchFinished {
	
    private final int batchID;

    public BatchFinished(int batchID) {

        this.batchID = batchID;
    }

    public int getBatchID() {
        return batchID;
    }
}
