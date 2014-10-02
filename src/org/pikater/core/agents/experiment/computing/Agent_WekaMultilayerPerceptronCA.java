package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.MultiLayerPerceptronCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;

public class Agent_WekaMultilayerPerceptronCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3792108496991664638L;

	@Override
	protected Classifier createClassifier() {
		
		return new MultilayerPerceptron();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return MultiLayerPerceptronCA_Box.get();
	}

}
