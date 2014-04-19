package org.pikater.core.options.xmlGenerators;

import org.pikater.core.dataStructures.slots.Slot;
import org.pikater.core.ontology.description.FileDataSaver;
import org.pikater.core.options.LogicalBoxDescription;

public class FileSaver_VirtualBox extends LogicalBoxDescription {

	public FileSaver_VirtualBox() {
		super("FileSaver",
				FileDataSaver.class,
				"This box save data to Pikater database"
				);
		
		this.setPicture("picture0.jpg");
		this.setAgentName(null);

		
		Slot inputSlot = new Slot();
		inputSlot.setDescription("Data counted in this experimnet");
		inputSlot.setType(Slot.SlotType.DATA_SLOT);
		inputSlot.setSex(Slot.SlotSex.CONSUMENT);
		inputSlot.setOntologyField(null);


		// Slot Definition
		this.addInputSlot(inputSlot);
	}

}
