package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.RBFNetworkCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.RBFNetwork;

public class Agent_WekaRBFNetworkCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3480796684001125058L;

	@Override
	protected Classifier createClassifier() {

		return new RBFNetwork();
	}


	@Override
	protected AgentInfo getAgentInfo() {

		return RBFNetworkCA_Box.get();
	}


}
