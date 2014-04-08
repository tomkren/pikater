package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class DifferenceVisualizerBox extends LogicalUnit
{
	
	protected DifferenceVisualizerBox() {
		
		this.setIsBox(true);
		this.setDisplayName("DifferenceVisualizer");
		this.setAgentName("Agent_DifferenceVisualizer");
		this.setType(BoxType.VISUALIZER);
		this.setOntology(null);
		this.setPicture("picture4.jpg");
		this.setDescription("Shows difference between input and model data sources.");

		addInputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "target data source"));
		addInputSlot(SimpleSlot.getDataSlot(FileInputBoxResources.data_parsedInput, "model data source"));
	}
}
