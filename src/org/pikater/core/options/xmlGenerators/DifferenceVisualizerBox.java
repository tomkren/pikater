package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.TypesOfBox;
import org.pikater.shared.experiment.slots.DataSlot;

public class DifferenceVisualizerBox extends LogicalUnit
{
	
	protected DifferenceVisualizerBox() {
		
		this.setIsBox(true);
		this.setName("DifferenceVisualizer");
		this.setAgentName("Agent_DifferenceVisualizer");
		this.setType(TypesOfBox.VISUALIZER_BOX);
		this.setOntology(null);
		this.setPicture("picture4.jpg");
		this.setDescription("Shows difference between input and model data sources.");

		this.addInputSlots(new DataSlot("Target Data Source"));
		this.addInputSlots(new DataSlot("Model Data Source"));
	}
}
