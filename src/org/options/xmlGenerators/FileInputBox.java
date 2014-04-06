package org.options.xmlGenerators;

import org.options.LogicalUnit;
import org.shared.boxTypes.TypesOfBox;
import org.shared.parameters.ValueParameter;
import org.shared.slots.DataSlot;


public class FileInputBox extends LogicalUnit {
	
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