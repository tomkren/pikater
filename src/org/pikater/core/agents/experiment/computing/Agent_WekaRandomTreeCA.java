package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.xmlGenerators.RandomTree_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomTree;

public class Agent_WekaRandomTreeCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8782398475553889105L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new RandomTree();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return RandomTree_CABox.get();
	}

}
