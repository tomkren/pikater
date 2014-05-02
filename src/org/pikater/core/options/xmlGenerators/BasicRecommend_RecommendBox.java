package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.recommend.Agent_Basic;
import org.pikater.core.ontology.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.agentInfo.Slot;
import org.pikater.core.ontology.description.Recommend;

public class BasicRecommend_RecommendBox {

	public static AgentInfo get() {

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("recomend");
		outputSlot.setDataType(SlotTypes.RECOMEND_TYPE);
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_Basic.class.getName());
		agentInfo.setOntologyClass(Recommend.class.getName());
	
		agentInfo.setName("Basic Recommend");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Basic Recommend");

		agentInfo.setOutputSlots(AAA_SlotHelper.getRecommendOutputSlots());
		
		return agentInfo;
	}

}
