package org.pikater.core.agents.system.computation.graph;

/**
 * Computation node for file saving
 * User: Kuba
 * Date: 19.6.2014
 * Time: 9:51
 */
public class FileSaverNode extends ComputationNode {
    /**
     *
     * @param executeStrategy Strategy to execute
     */
    public FileSaverNode(StartComputationStrategy executeStrategy) {
        super(executeStrategy);
    }

    public FileSaverNode(ComputationGraph computationGraph) {
        super(computationGraph);
    }
}
