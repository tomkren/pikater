package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.FileVisualizer;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.experiment.BoxType;

public class FileVisualizer_VirtualBox extends LogicalBoxDescription
{
	protected FileVisualizer_VirtualBox()
	{
		super("FileVisualizer",
				FileVisualizer.class,
				"Displays provided data."
				);

		this.setPicture("picture4.jpg");
		this.setAgentName(null);
		
		// this.addInputSlot(new DataSlot("Data Source"));
	}
}