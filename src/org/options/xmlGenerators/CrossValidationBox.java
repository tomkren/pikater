package org.options.xmlGenerators;

import org.options.LogicalUnit;
import org.shared.boxTypes.TypesOfBox;
import org.shared.slots.DataSlot;


public class CrossValidationBox extends LogicalUnit {
	
	protected CrossValidationBox() {

		this.setIsBox(true);
		this.setName("CrossValidation-Method");
		this.setAgentName("Agent_CrossValidation");
		this.setType(TypesOfBox.COMPUTING_BOX);
		this.setOntology(null);
		this.setPicture("picture3.jpg");
		this.setDescription("Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method.");
		
		this.addInputSlots(new DataSlot("Training data"));
		this.addInputSlots(new DataSlot("Testing data"));
		this.addInputSlots(new DataSlot("Validation data"));
		
		this.addOutputSlots(new DataSlot("Computed data"));
	}
}