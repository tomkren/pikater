package org.pikater.core.options.xmlGenerators;

import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.dataStructures.slots.Slot;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.options.LogicalBoxDescription;

public class FileInput_VirtualBox extends LogicalBoxDescription{
	
	protected FileInput_VirtualBox() {
		super("FileInput",
				FileDataProvider.class,
				"This box provides a data source to other boxes."
				);

		this.setPicture("picture0.jpg");
		this.setAgentName(null);
		
		OptionDefault optionIN = new OptionDefault();
		optionIN.setName("File");
		optionIN.setDescription("");
		optionIN.setValue(
				new OptionValue(new String("inputFile.ARFF")) );
		optionIN.setInterval(null);
		optionIN.setList(null);
		
		this.addOption(optionIN);
		
		
		Slot outputSlot = new Slot();
		outputSlot.setDescription("Data loaded from dataset file");
		outputSlot.setType(Slot.SlotType.DATA_SLOT);
		outputSlot.setSex(Slot.SlotSex.PRODUCENT);
		outputSlot.setOntologyField(null);



		// Slot Definition
		this.addOutputSlot(outputSlot);
	}
}