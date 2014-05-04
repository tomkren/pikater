package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.PART_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.rules.PART;

public class Agent_WekaPARTCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3540915204245346680L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new PART();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return PART_CABox.get();
	}

}
