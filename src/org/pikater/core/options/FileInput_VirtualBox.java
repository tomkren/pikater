package org.pikater.core.options;

import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;

public class FileInput_VirtualBox {
	
	public static AgentInfo get() {

		OptionDefault optionIN = new OptionDefault();
		optionIN.setName("File");
		optionIN.setDescription("");
		optionIN.setValue(
				new OptionValue(new String("inputFile.ARFF")) );
		optionIN.setInterval(null);
		optionIN.setList(null);

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		// TODO: some virtual-box provider agent
		agentInfo.setAgentClass(null);
		agentInfo.setOntologyClass(FileDataProvider.class.getName());
	
		agentInfo.setName("FileInput");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("This box provides a data source to other boxes.");

		agentInfo.addOption(optionIN.toOption());
		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}