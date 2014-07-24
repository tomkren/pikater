package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.OneRCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.rules.OneR;

public class Agent_WekaOneRCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8673671279677606903L;

	@Override
	protected Classifier getClassifierClass() {

		return new OneR();
	}

	@Override
	protected AgentInfo getAgentInfo() {

		return OneRCA_Box.get();
	}

}
