package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationStrategies;

import org.pikater.core.agents.system.Agent_Manager;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.ComputationNode;
import org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph.StartComputationStrategy;
import org.pikater.core.agents.system.computationDescriptionParser.edges.DataSourceEdge;

/**
 * User: Kuba
 * Date: 29.6.2014
 * Time: 16:12
 */
public class FileSavingStrategy implements StartComputationStrategy {
    Agent_Manager myAgent;
    int computationId;
    int graphId;
    ComputationNode computationNode;

    public FileSavingStrategy (Agent_Manager manager, int computationId,
                                       int graphId, ComputationNode computationNode){
        myAgent = manager;
        this.computationId = computationId;
        this.graphId = graphId;
        this.computationNode = computationNode;
    }

    @Override
    public void execute(ComputationNode computation) {
        myAgent.log("This will be the place where the data will be saved.");
        DataSourceEdge dataToSave=(DataSourceEdge)computationNode.getInputs().get("file").getNext();
        computationNode.computationFinished();
    }
}
