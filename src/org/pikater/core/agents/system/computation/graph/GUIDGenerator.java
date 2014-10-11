package org.pikater.core.agents.system.computation.graph;

/**
 * Global unique ID generator interface
 * User: Kuba
 * Date: 14.5.2014
 * Time: 11:37
 */
public interface GUIDGenerator {
    /**
     * Gets and allocate gUID.
     *
     * @return the and allocate gUID
     */
    Integer getAndAllocateGUID();
}
