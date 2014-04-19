package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class NBTree_CABox extends LogicalBoxDescription {
	public NBTree_CABox(){
		super("NBTree",ComputingAgent.class,"NBTree MEthod");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
	}
}
