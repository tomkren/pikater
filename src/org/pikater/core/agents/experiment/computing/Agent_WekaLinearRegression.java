package org.pikater.core.agents.experiment.computing;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.options.computing.LinearRegressionCA_Box;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;

public class Agent_WekaLinearRegression extends Agent_WekaAbstractCA {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3947508050679743L;

	@Override
	protected Classifier createClassifier() {
		
		return new LinearRegression();
	}
	
	@Override
	protected AgentInfo getAgentInfo() {
		
		return LinearRegressionCA_Box.get();
	}

}
