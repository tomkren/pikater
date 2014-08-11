package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNBTreeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class NBTreeCA_Box {
	
	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaNBTreeCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NBTree");
		agentInfo.setDescription("NBTree Method");
		agentInfo.addOptions(OptionsHelper.getCAOptions());
		agentInfo.addOptions(OptionsHelper.getCAorRecommenderOptions());

		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getSlots_CAInput());
		agentInfo.setOutputSlots(SlotsHelper.getSlots_CAOutput());

		return agentInfo;
	}
}
