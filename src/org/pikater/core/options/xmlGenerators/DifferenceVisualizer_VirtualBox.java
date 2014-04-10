package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.DifferenceVisualizer;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class DifferenceVisualizer_VirtualBox extends LogicalBoxDescription {
	
	protected DifferenceVisualizer_VirtualBox() {
		super("DifferenceVisualizer",
				DifferenceVisualizer.class,
				"Shows difference between input and model data sources."
				);
		
		this.setPicture("picture4.jpg");
		this.setAgentName(null);

		addInputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "target data source"));
		addInputSlot(SimpleSlot.getDataSlot(FileInputBox_VirtualBoxResources.data_parsedInput, "model data source"));
	}
}
