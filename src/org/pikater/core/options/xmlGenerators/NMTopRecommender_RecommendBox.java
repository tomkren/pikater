package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.recommend.Agent_NMTopRecommender;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.Recommend;

public class NMTopRecommender_RecommendBox {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_NMTopRecommender.class.getName());
		agentInfo.setOntologyClass(Recommend.class.getName());
	
		agentInfo.setName("NMTop Recommend");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("NMTop Recommend");

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getRecommendOutputSlots());

		return agentInfo;
	}

}
