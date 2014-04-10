package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class FileInput_VirtualBox extends LogicalBoxDescription
{
	
	protected FileInput_VirtualBox() {
		super("FileInput",
				FileDataProvider.class,
				"This box provides a data source to other boxes."
				);

		this.setPicture("picture0.jpg");
		this.setAgentName(null);
		
		// this.addParameter(ParameterVisibility.USER_EDITABLE, FileInputBoxResources.param_inputFileName, new ValueParameter<String>("file.txt"));
		this.addOutputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "Data parsed from provided file name and channeled into this output slot."));
	}
}