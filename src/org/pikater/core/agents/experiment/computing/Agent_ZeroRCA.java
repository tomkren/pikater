package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.ZeroRCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;

public class Agent_ZeroRCA  extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5635255217519444219L;

	@Override
	protected Classifier createClassifier() {

		return new ZeroR();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return ZeroRCA_Box.get();
	}

}
