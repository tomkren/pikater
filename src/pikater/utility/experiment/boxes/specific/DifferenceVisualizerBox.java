package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.slots.DataSlot;

public class DifferenceVisualizerBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;
	
	public DifferenceVisualizerBox()
	{
		super("DifferenceVisualizer", "picture5.jpg", "Shows difference between input and model data sources.", "Agent_DifferenceVisualizer", "DifferenceVisualizer");
		
		this.categories = EnumSet.of(LeafBoxCategory.VISUALIZER);

		addInputSlot(new DataSlot("Target Data Source"));
		addInputSlot(new DataSlot("Model Data Source"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
