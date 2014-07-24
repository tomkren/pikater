package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.SMOCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;

public class Agent_WekaSMOCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5835079000365136450L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new SMO();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return SMOCA_Box.get();
	}

}
