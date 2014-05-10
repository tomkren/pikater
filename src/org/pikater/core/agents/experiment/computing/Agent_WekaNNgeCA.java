package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.options.NNge_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.rules.NNge;

public class Agent_WekaNNgeCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4300329613493738992L;

	@Override
	protected Classifier getClassifierClass() {

		return new NNge();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return NNge_CABox.get();
	}

}