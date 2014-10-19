package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.options.computing.NaiveBayesCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

public class Agent_WekaNaiveBayesCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8375125527735841244L;

	@Override
	protected Classifier createClassifier() {
		
		return new NaiveBayes();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return NaiveBayesCA_Box.get();
	}

}
