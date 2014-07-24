package org.pikater.core.agents.system.computationDescriptionParser.dependencyGraph;

import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;

/**
 * User: Kuba
 * Date: 18.5.2014
 * Time: 15:52
 */
public class ModelComputationNode extends ComputationNode {
    private String modelClass;
    private EvaluationMethod evaluationMethod;
    private IExpectedDuration expectedDuration;

    public ModelComputationNode() {
        super();
    }

    public ModelComputationNode(StartComputationStrategy executeStrategy) {
        super(executeStrategy);
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
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
}
