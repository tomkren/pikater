package pikater.utility.boxTypes;

import pikater.ontology.description.DifferenceVisualizer;
import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.slots.DataConsumerSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class DifferenceVisualizerBox extends Box
{
	public DifferenceVisualizerBox()
	{
		super("DifferenceVisualizer", "pikater/Agent_DifferenceVisualizer", BoxCategory.VISUALIZER.name(), DifferenceVisualizer.class, "picture5.jpg", "Visualiser shows difference between "
				+ "target data input and model data input.");
		
		UniversalSlot boxTargetInputSlots = new DataConsumerSlot();
		boxTargetInputSlots.setSlotName("Target Data Source");
		addInputSlot(boxTargetInputSlots);

		UniversalSlot boxModelInputSlots = new DataConsumerSlot();
		boxModelInputSlots.setSlotName("Model Data Source");
		addInputSlot(boxModelInputSlots);
	}
}
