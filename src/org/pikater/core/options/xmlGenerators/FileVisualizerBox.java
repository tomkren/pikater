package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;

public class FileVisualizerBox extends LogicalUnit
{
	protected FileVisualizerBox()
	{
		this.setIsBox(true);
		this.setDisplayName("FileVisualizer");
		this.setAgentName("pikater/Agent_Visualizer");
		this.setType(BoxType.VISUALIZER);
		this.setOntology(null);
		this.setPicture("picture4.jpg");
		this.setDescription("Displays provided data.");
		
		// this.addInputSlot(new DataSlot("Data Source"));
	}
}