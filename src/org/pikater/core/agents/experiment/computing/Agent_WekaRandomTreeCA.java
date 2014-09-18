package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.RandomTreeCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomTree;

public class Agent_WekaRandomTreeCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8782398475553889105L;

	@Override
	protected Classifier createClassifier() {
		
		return new RandomTree();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return RandomTreeCA_Box.get();
	}

}
