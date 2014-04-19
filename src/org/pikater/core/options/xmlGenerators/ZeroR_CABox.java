package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class ZeroR_CABox extends LogicalBoxDescription {
	public ZeroR_CABox(){
		super("ZeroR",ComputingAgent.class,"Zero R Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		// Slots Definition
		this.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		this.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	}
}
