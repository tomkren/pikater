package org.pikater.core.agents.system.computation.graph.strategies;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computation.graph.ComputationNode;
import org.pikater.core.agents.system.computation.graph.StartComputationStrategy;
import org.pikater.core.agents.system.computation.parser.ComputationOutputBuffer;

import java.util.Map;

/**
 * Strategy for saving files
 * User: Kuba
 * Date: 29.6.2014
 * Time: 16:12
 */
public class FileSavingStrategy implements StartComputationStrategy {
    Agent_Manager myAgent;
    ComputationNode computationNode;

    /**
     *  @param manager Manager agent
     * @param computationNode Parent computation node*/
    public FileSavingStrategy(Agent_Manager manager, ComputationNode computationNode){
        myAgent = manager;
        this.computationNode = computationNode;
    }

    /**
     *
     * @param computation Computation node with this strategy
     */
    @Override
    public void execute(ComputationNode computation) {
        myAgent.logInfo("This will be the place where the data will be saved.");
        Map<String,ComputationOutputBuffer> map = computationNode.getInputs();
        ComputationOutputBuffer buffer = map.get("file");
        computationNode.computationFinished();
    }
}
