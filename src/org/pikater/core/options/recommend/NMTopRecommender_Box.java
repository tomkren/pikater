package org.pikater.core.options.recommend;

import org.pikater.core.agents.experiment.recommend.Agent_NMTopRecommender;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.options.AgentDefinitionHelper;

public class NMTopRecommender_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_NMTopRecommender.class);
		agentInfo.importOntologyClass(Recommend.class);
	
		agentInfo.setName("NMTop");
		agentInfo.setDescription("NMTop Recommend");

		//Slot Definition
		agentInfo.setOutputSlots(AgentDefinitionHelper.getRecommendOutputSlots());

		return agentInfo;
	}

}
