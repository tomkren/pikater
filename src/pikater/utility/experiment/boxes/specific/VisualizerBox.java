package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.slots.DataSlot;

public class VisualizerBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;
	
	public VisualizerBox()
	{
		super("Visualizer", "picture4.jpg", "Visualiser shows dates from dataSource.", "pikater/Agent_Visualizer", "FileVisualizer");
		
		this.categories = EnumSet.of(LeafBoxCategory.VISUALIZER);
		
		addInputSlot(new DataSlot("Data Source"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
