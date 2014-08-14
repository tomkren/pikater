package org.pikater.core.options.recommend;

import org.pikater.core.agents.experiment.recommend.Agent_NMTopRecommender;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.options.SlotsHelper;

public class NMTopRecommender_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_NMTopRecommender.class);
		agentInfo.importOntologyClass(Recommend.class);
	
		agentInfo.setName("NMTop");
		agentInfo.setDescription("NMTop Recommend");
		
		//Slot Definition
		agentInfo.setInputSlots(SlotsHelper.getInputSlots_Recommend());
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_Recommend());

		return agentInfo;
	}

}
