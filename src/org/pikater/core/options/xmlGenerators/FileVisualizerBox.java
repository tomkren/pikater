package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.TypesOfBox;
import org.pikater.shared.experiment.slots.DataSlot;

public class FileVisualizerBox extends LogicalUnit
{
	protected FileVisualizerBox()
	{
		this.setIsBox(true);
		this.setName("FileVisualizer");
		this.setAgentName("pikater/Agent_Visualizer");
		this.setType(TypesOfBox.VISUALIZER_BOX);
		this.setOntology(null);
		this.setPicture("picture4.jpg");
		this.setDescription("Visualiser shows dates from dataSource.");
		
		this.addInputSlots(new DataSlot("Data Source"));
	}
}