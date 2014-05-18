package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.Perceptron_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

public class Agent_WekaPerceptronCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9097793752774638033L;

	@Override
	protected Classifier getClassifierClass() {

		return new MultilayerPerceptron();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return Perceptron_CABox.get();
	}

}
