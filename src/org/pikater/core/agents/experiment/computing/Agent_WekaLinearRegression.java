package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.LinearRegression_CABox;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;

public class Agent_WekaLinearRegression extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3947508050679743L;

	@Override
	protected Classifier getClassifierClass() {
		
		return new LinearRegression();
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		
		return LinearRegression_CABox.get();
	}

}
