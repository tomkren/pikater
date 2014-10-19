package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNBTreeCA;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
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

		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getInputSlots_CA());
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_CA());

		return agentInfo;
	}
}
