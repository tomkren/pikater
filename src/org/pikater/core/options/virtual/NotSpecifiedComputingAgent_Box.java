package org.pikater.core.options.virtual;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class NotSpecifiedComputingAgent_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(null);
		agentInfo.importOntologyClass(ComputingAgent.class);

		agentInfo.setName("NotSpecifiedCA");
		agentInfo.setDescription("Not Specified Computing Agent");
		
		agentInfo.addOptions(
				OptionsHelper.getNotSpecifiedCAOptions());
		agentInfo.setInputSlots(
				SlotsHelper.getInputSlots_CA());

		return agentInfo;

	}

}
