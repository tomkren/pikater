package org.pikater.core.options.virtual;

import org.pikater.core.agents.experiment.virtual.Agent_VirtualFileInputBoxProvider;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class FileInput_Box {
	
	public static AgentInfo get() {

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_VirtualFileInputBoxProvider.class);
		agentInfo.importOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("FileInput");
		agentInfo.setDescription("This box provides a data source to other boxes.");

		NewOption optionIN = new NewOption("File", new StringValue("inputFile.ARFF"));
		optionIN.setDescription("File name");
		
		agentInfo.addOption(optionIN);
		
		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);
		
		agentInfo.addOutputSlot(outputSlot);

		return agentInfo;
	}

}