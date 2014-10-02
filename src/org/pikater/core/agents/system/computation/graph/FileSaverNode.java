package org.pikater.core.agents.system.computation.graph;

/**
 * User: Kuba
 * Date: 19.6.2014
 * Time: 9:51
 */
public class FileSaverNode extends ComputationNode {
    public FileSaverNode(StartComputationStrategy executeStrategy,ComputationGraph computationGraph) {
        super(executeStrategy,computationGraph);
    }

    public FileSaverNode(ComputationGraph computationGraph) {
        super(computationGraph);
    }
}
