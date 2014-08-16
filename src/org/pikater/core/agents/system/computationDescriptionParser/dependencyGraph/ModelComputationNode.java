package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:52
 */
public class ModelComputationNode extends ComputationNode {
    private EvaluationMethod evaluationMethod;
    private IExpectedDuration expectedDuration;
    private int priority;

    public ModelComputationNode(ComputationGraph computationGraph) {
        super(computationGraph);
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

	public IExpectedDuration getExpectedDuration() {
		return expectedDuration;
	}

	public void setExpectedDuration(IExpectedDuration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getExperimentID() {
		return computationGraph.getExperimentId();
	}
}
