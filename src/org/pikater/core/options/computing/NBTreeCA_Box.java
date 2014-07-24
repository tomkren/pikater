package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNBTreeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.options.AgentDefinitionHelper;

public class NBTreeCA_Box {
	
	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaNBTreeCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NBTree");
		agentInfo.setDescription("NBTree Method");
		agentInfo.addOptions(AgentDefinitionHelper.getCAOptions());

		// Slots Definition
		agentInfo.setInputSlots(AgentDefinitionHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AgentDefinitionHelper.getCAOutputSlots());

		return agentInfo;
	}
}
