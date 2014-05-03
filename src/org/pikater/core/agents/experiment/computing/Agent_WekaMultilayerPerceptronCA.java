package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.xmlGenerators.MultiLayerPerceptron_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

public class Agent_WekaMultilayerPerceptronCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3792108496991664638L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new MultilayerPerceptron();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return MultiLayerPerceptron_CABox.get();
	}

}
