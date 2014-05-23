package org.pikater.core.options;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;

public class FileSaver_VirtualBox  {

	public static AgentInfo get() {

		Slot inputSlot = new Slot();
		inputSlot.setSlotType("data");
		inputSlot.setDataType(SlotTypes.DATA_TYPE);
		
		AgentInfo agentInfo = new AgentInfo();
		//TODO: some virtual-box provider agent
		agentInfo.setAgentClass(Agent_VirtualBoxProvider.class.getName());
		agentInfo.setOntologyClass(FileDataSaver.class.getName());
	
		agentInfo.setName("FileSaver");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("This box save data to Pikater database");

		agentInfo.addInputSlot(inputSlot);

		return agentInfo;

	}

}
