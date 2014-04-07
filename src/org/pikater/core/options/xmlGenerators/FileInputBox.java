package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.TypesOfBox;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.experiment.slots.DataSlot;

public class FileInputBox extends LogicalUnit
{
	
	protected FileInputBox() {

		this.setIsBox(true);
		this.setName("FileInput");
		this.setAgentName("null");
		this.setType(TypesOfBox.INPUT_BOX);
		this.setOntology(null);
		this.setPicture("picture0.jpg");
		this.setDescription("This box provides a data source to other boxes.");
		
		this.addParameter(new ValueParameter<String>("file.txt"));
		
		this.addOutputSlots(new DataSlot("Dataset"));
	}
}