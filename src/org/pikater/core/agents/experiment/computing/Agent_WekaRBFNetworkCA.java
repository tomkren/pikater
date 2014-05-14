package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.RBFNetwork_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.functions.RBFNetwork;

public class Agent_WekaRBFNetworkCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3480796684001125058L;

	@Override
	protected Classifier getClassifierClass() {

		return new RBFNetwork();
	}


	@Override
	protected AgentInfo getAgentInfo() {

		return RBFNetwork_CABox.get();
	}


}
