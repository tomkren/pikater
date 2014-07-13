package org.pikater.core.options;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualFileInputBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class FileInput_VirtualBox {
	
	public static AgentInfo get() {

		NewOption optionIN = new NewOption("File", new StringValue("inputFile.ARFF"));
		optionIN.setDescription("File name");
		

		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_VirtualFileInputBoxProvider.class);
		agentInfo.setOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("FileInput");
		agentInfo.setDescription("This box provides a data source to other boxes.");

		agentInfo.addOption(optionIN);
		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}