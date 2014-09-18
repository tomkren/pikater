package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

/**
 * User: Kuba
 * Date: 16.8.2014
 * Time: 14:00
 */
public class DataProcessingComputationNode extends ComputationNode {
    private int numberOfInputs;
    private PikaterAgent agent;
    private int experimentID;

    public DataProcessingComputationNode(ComputationGraph computationGraph,
    		PikaterAgent agent, int experimentID) {
        super(computationGraph);
        this.agent = agent;
        this.experimentID = experimentID;
    }

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public void setNumberOfInputs(int numberOfInputs) {
        this.numberOfInputs = numberOfInputs;
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
