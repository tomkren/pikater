package pikater.utility.boxTypes;

import pikater.ontology.description.FileVisualizer;
import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class VisualizerBox extends Box
{
	public VisualizerBox()
	{
		super("Visualizer", "pikater/Agent_Visualizer", BoxCategory.VISUALIZER.name(), FileVisualizer.class, "picture4.jpg", "Visualiser shows dates from dataSource.");
		
		UniversalSlot boxDataInputSlots = new DataConsumerSlot();
		boxDataInputSlots.setSlotName("Data Source");
		addInputSlot(boxDataInputSlots);
	}
}
