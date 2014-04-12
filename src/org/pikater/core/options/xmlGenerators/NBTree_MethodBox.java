package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;

public class NBTree_MethodBox extends LogicalBoxDescription {
	public NBTree_MethodBox(){
		super("NBTree",Method.class,"NBTree MEthod");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
	}
}
