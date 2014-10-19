package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.options.computing.NNgeCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.rules.NNge;

public class Agent_WekaNNgeCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4300329613493738992L;

	@Override
	protected Classifier createClassifier() {

		return new NNge();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return NNgeCA_Box.get();
	}

}
