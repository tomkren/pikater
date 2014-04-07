package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class CrossValidationBox extends LogicalUnit
{
	protected CrossValidationBox()
	{
		this.setIsBox(true);
		this.setDisplayName("CrossValidation-Method");
		this.setAgentName("Agent_CrossValidation");
		this.setType(BoxType.COMPUTING);
		this.setOntology(null);
		this.setPicture("picture3.jpg");
		this.setDescription("Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method.");

		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "training data"));
		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "testing data"));
		this.addInputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "validation data"));

		this.addOutputSlot(SimpleSlot.getDataSlot(CrossValidationBoxResources.data_computed, "data computed by this box and provided to this output slot"));
	}
}