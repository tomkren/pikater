package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.options.computing.PerceptronCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

public class Agent_WekaPerceptronCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9097793752774638033L;

	@Override
	protected Classifier createClassifier() {

		return new MultilayerPerceptron();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return PerceptronCA_Box.get();
	}

}
