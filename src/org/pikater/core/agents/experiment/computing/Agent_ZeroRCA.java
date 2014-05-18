package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.ZeroR_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;

public class Agent_ZeroRCA  extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5635255217519444219L;

	@Override
	protected Classifier getClassifierClass() {

		return new ZeroR();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return ZeroR_CABox.get();
	}

}
