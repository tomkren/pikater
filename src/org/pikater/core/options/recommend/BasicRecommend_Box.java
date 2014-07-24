package org.pikater.core.options.recommend;

import org.pikater.core.agents.experiment.recommend.Agent_Basic;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.options.AgentDefinitionHelper;

public class BasicRecommend_Box {

	public static AgentInfo get() {

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("recomend");
		outputSlot.setDataType(SlotTypes.RECOMMEND_TYPE);
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_Basic.class);
		agentInfo.importOntologyClass(Recommend.class);
	
		agentInfo.setName("Basic");
		agentInfo.setDescription("Basic Recommend");

		agentInfo.setOutputSlots(AgentDefinitionHelper.getRecommendOutputSlots());
		
		return agentInfo;
	}

}
