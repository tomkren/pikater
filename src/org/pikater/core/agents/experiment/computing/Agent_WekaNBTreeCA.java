package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.NBTreeCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.trees.NBTree;

public class Agent_WekaNBTreeCA extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2430007385203181066L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new NBTree();
	}

	@Override
	protected AgentInfo getAgentInfo() {
		
		return NBTreeCA_Box.get();
	}

}
