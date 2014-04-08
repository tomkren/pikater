package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.experiment.slots.SimpleSlot;
import org.pikater.web.experiment.box.LeafBox.ParameterVisibility;

public class FileInputBox extends LogicalUnit
{
	
	protected FileInputBox() {

		this.setIsBox(true);
		this.setDisplayName("FileInput");
		this.setAgentName("null");
		this.setType(BoxType.INPUT);
		this.setOntology(null);
		this.setPicture("picture0.jpg");
		this.setDescription("This box provides a data source to other boxes.");
		
		// this.addParameter(ParameterVisibility.USER_EDITABLE, FileInputBoxResources.param_inputFileName, new ValueParameter<String>("file.txt"));
		this.addOutputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "Data parsed from provided file name and channeled into this output slot."));
	}
}