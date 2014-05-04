package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.J48_CABox;

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

		return J48_CABox.get();
	}

}
