package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

/**
 * Computation node for data pro and pre processing
 * User: Kuba
 * Date: 16.8.2014
 * Time: 14:00
 */
public class DataProcessingComputationNode extends ComputationNode {
    private PikaterAgent agent;
    private int experimentID;

    /**
     *
     * @param computationGraph Parent computation graph
     * @param agent Owning agent
     * @param experimentID Id of the experiment
     */
    public DataProcessingComputationNode(ComputationGraph computationGraph,
    		PikaterAgent agent, int experimentID) {
        super(computationGraph);
        this.agent = agent;
        this.experimentID = experimentID;
    }
    
	@Override
    public void numberOfTasksChanged() {
		
        if (numberOfTasksInProgress==0) {
        	
    		DataManagerService.updateExperimentStatus(
    				this.agent, experimentID, JPAExperimentStatus.FINISHED.name());
            computationGraph.updateState();
        }
    }
}
