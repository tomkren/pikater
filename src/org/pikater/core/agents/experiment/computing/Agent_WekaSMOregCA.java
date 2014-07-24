package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.SMORegCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMOreg;

public class Agent_WekaSMOregCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239067912711714537L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new SMOreg();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return SMORegCA_Box.get();
	}

}
