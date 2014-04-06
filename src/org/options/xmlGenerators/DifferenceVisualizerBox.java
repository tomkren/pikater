package org.options.xmlGenerators;

import org.options.LogicalUnit;
import org.shared.boxTypes.TypesOfBox;
import org.shared.slots.DataSlot;


public class DifferenceVisualizerBox extends LogicalUnit {
	
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
