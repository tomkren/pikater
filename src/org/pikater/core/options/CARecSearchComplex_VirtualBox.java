package org.pikater.core.options;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualCARecSearchComplexBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.CARecSearchComplex;

public class CARecSearchComplex_VirtualBox {

	public static AgentInfo get() {

		Slot inputAgentSlot = new Slot();
		inputAgentSlot.setDescription("data computed");
		inputAgentSlot.setSlotType(SlotTypes.AGENT_TYPE);
		inputAgentSlot.setDataType("data");

		Slot inputRecomendSlot = new Slot();
		inputRecomendSlot.setDescription("Name of agent");
		inputRecomendSlot.setDataType(SlotTypes.RECOMEND_TYPE);
		inputRecomendSlot.setDataType("recomend");

		Slot inputSearcherSlot = new Slot();
		inputSearcherSlot.setDescription("Parameters produced by searcher");
		inputSearcherSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		inputSearcherSlot.setDataType("parameters");

		Slot outputSlot = new Slot();
		outputSlot.setDescription("data computed");
		outputSlot.setSlotType(SlotTypes.AGENT_TYPE);
		outputSlot.setDataType("data");

		AgentInfo agentInfo = new AgentInfo();
		agentInfo
				.setAgentClass(Agent_VirtualCARecSearchComplexBoxProvider.class);
		agentInfo.setOntologyClass(CARecSearchComplex.class);

		agentInfo.setName("Complex");
		agentInfo.setDescription("Complex Box");

		agentInfo.addInputSlot(inputAgentSlot);
		agentInfo.addInputSlot(inputRecomendSlot);
		agentInfo.addInputSlot(inputSearcherSlot);

		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}
