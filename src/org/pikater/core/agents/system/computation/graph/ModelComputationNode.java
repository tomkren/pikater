package org.pikater.core.agents.system.computation.graph;

import org.pikater.core.agents.PikaterAgent;
import org.pikater.core.agents.system.data.DataManagerService;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration;
import org.pikater.shared.database.jpa.status.JPAExperimentStatus;

/**
 * Computation node for executing ML experiments
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

    /**
     *
     * @param computationGraph Parent graph
     * @param agent Owner agent
     * @param experimentID Id of parent experiment
     */
    public ModelComputationNode(ComputationGraph computationGraph,
    		PikaterAgent agent, int experimentID) {
        super(computationGraph);
        this.agent = agent;
        this.experimentID = experimentID;
    }

    /**
     *
     * @param executeStrategy Strategy to be executes
     */
    public ModelComputationNode(StartComputationStrategy executeStrategy) {
        super(executeStrategy);
    }

    /**
     * Get method for eval, e.g. cross validation
     * @return Evaluation method
     */
	public EvaluationMethod getEvaluationMethod() {
		return evaluationMethod;
	}

    /**
     * Sets method for eval
     * @param evaluationMethod Method for evaluation
     */
	public void setEvaluationMethod(EvaluationMethod evaluationMethod) {
		this.evaluationMethod = evaluationMethod;
	}

    /**
     *
     * @return Expected duration
     */
	public ExpectedDuration getExpectedDuration() {
		return expectedDuration;
	}

    /**
     * Sets expected duration
     * @param expectedDuration Expected duration to be set
     */
	public void setExpectedDuration(ExpectedDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

    /**
     * Gets priority
     * @return Priority of the node
     */
	public int getPriority() {
		return priority;
	}

    /**
     * Sets priority of the node
     * @param priority Priority
     */
	public void setPriority(int priority) {
		this.priority = priority;
	}

    /**
     * Get batch of the parent graph
     * @return  Batch id
     */
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
