package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:52
 */
public class ModelComputationNode extends ComputationNode {
    private EvaluationMethod evaluationMethod;
    private ExpectedDuration expectedDuration;
    private PikaterAgent agent;
    private int experimentID;
    private int priority;

    public ModelComputationNode(ComputationGraph computationGraph,
    		PikaterAgent agent, int experimentID) {
        super(computationGraph);
        this.agent = agent;
        this.experimentID = experimentID;
    }

    public ModelComputationNode(StartComputationStrategy executeStrategy,ComputationGraph computationGraph) {
        super(executeStrategy,computationGraph);
    }

	public EvaluationMethod getEvaluationMethod() {
		return evaluationMethod;
	}

	public void setEvaluationMethod(EvaluationMethod evaluationMethod) {
		this.evaluationMethod = evaluationMethod;
	}

	public ExpectedDuration getExpectedDuration() {
		return expectedDuration;
	}

	public void setExpectedDuration(ExpectedDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getBatchID() {
		return computationGraph.getBatchID();
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
