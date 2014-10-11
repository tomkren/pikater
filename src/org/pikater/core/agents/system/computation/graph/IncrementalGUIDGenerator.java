package org.pikater.core.agents.system.computation.graph;

/**
 * Incremental guid generator
 * User: Kuba
 * Date: 14.5.2014
 * Time: 11:39
 */
public class IncrementalGUIDGenerator implements GUIDGenerator {
    public static Integer lastGuid=0;

    @Override
    public Integer getAndAllocateGUID() {
        lastGuid++;
        return lastGuid;
    }
}
