package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.J48CA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

public class Agent_WekaJ48 extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3338557556876835327L;

	@Override
	protected Classifier getClassifierClass() {

		return new J48();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return J48CA_Box.get();
	}

}
