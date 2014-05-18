package org.pikater.core.options;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.description.FileDataSaver;

public class FileSaver_VirtualBox  {

	public static AgentInfo get() {

		Slot inputSlot = new Slot();
		inputSlot.setSlotType("data");
		inputSlot.setDataType(SlotTypes.DATA_TYPE);
		
		AgentInfo agentInfo = new AgentInfo();
		//TODO: some virtual-box provider agent
		agentInfo.setAgentClass(null);
		agentInfo.setOntologyClass(FileDataSaver.class.getName());
	
		agentInfo.setName("FileSaver");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("This box save data to Pikater database");

		agentInfo.addInputSlot(inputSlot);

		return agentInfo;

	}

}
