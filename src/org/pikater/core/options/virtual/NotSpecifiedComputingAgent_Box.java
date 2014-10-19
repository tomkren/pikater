package org.pikater.core.options.virtual;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class NotSpecifiedComputingAgent_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClassName(null);
		agentInfo.importOntologyClass(ComputingAgent.class);

		agentInfo.setName("NotSpecifiedCA");
		agentInfo.setDescription("Not Specified Computing Agent");
		
		agentInfo.addOptions(
				OptionsHelper.getNotSpecifiedCAOptions());
		agentInfo.setInputSlots(
				SlotsHelper.getInputSlots_CA());
		agentInfo.setOutputSlots(
				SlotsHelper.getOutputSlots_CA());
		
		return agentInfo;

	}

}
