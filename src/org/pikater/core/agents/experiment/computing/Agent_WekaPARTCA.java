package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.PARTCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.rules.PART;

public class Agent_WekaPARTCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3540915204245346680L;

	@Override
	protected Classifier createClassifier() {
		
		return new PART();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return PARTCA_Box.get();
	}

}
