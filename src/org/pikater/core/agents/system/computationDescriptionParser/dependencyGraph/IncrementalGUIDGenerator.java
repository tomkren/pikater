package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

/**
 * User: Kuba
 * Date: 14.5.2014
 * Time: 11:39
 */
public class IncrementalGUIDGenerator implements GUIDGenerator {
    Integer lastGuid=0;

    @Override
    public Integer getAndAllocateGUID() {
        lastGuid++;
        return lastGuid;
    }
}
