package org.pikater.core.options;

import org.pikater.core.agents.experiment.recommend.Agent_NMTopRecommender;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;

public class NMTopRecommender_RecommendBox {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_NMTopRecommender.class);
		agentInfo.importOntologyClass(Recommend.class);
	
		agentInfo.setName("NMTop Recommend");
		agentInfo.setDescription("NMTop Recommend");

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getRecommendOutputSlots());

		return agentInfo;
	}

}
