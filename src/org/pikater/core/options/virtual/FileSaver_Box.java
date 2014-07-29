package org.pikater.core.options.virtual;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualFileSaverBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;

public class FileSaver_Box {

	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_VirtualFileSaverBoxProvider.class);
		agentInfo.importOntologyClass(FileDataSaver.class);

		agentInfo.setName("FileSaver");
		agentInfo.setDescription("This box save data to Pikater database");

		Slot inputSlot = new Slot();
		inputSlot.setSlotType("data");
		inputSlot.setDataType(SlotTypes.DATA_TYPE);
		
		agentInfo.addInputSlot(inputSlot);

		return agentInfo;

	}

}
