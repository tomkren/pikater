package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_ZeroRCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class ZeroRCA_Box {
	
	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_ZeroRCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("ZeroR");
		agentInfo.setDescription("Zero R Method");

		agentInfo.addOptions(OptionsHelper.getCAOptions());
		
		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getInputSlots_CA());
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_CA());
		
		return agentInfo;
	}
}
