package pikater.utility.boxTypes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoxesModel
{
	/**
	 * Changes to this enum's constants have global effect. Nothing else needs to changed.
	 */
	public enum BoxCategory
	{
		INPUT,
		SEARCHER,
		COMPUTING,
		VISUALIZER
	};
	
	/**
	 * Changes to these constants have global effect. Nothing else needs to changed.
	 */
	public static final String box_fileInput = InputBox.class.getSimpleName();
	public static final String box_randomSearch = RandomSearchBox.class.getSimpleName();
	public static final String box_simulatedAnnealing = SimulatedAnnealingBox.class.getSimpleName();
	public static final String box_crossValidation = CrossValidationBox.class.getSimpleName();
	public static final String box_visualizer = VisualizerBox.class.getSimpleName();
	public static final String box_differenceVisualizer = DifferenceVisualizerBox.class.getSimpleName();

	public static final Map<String, Box> boxes = new HashMap<String, Box>();
	static
	{
		{
			// Static initialization. Automatically imports the designated boxes.
			
			boxes.put(box_fileInput, new InputBox());
			boxes.put(box_randomSearch, new RandomSearchBox());
			boxes.put(box_simulatedAnnealing, new SimulatedAnnealingBox());
			boxes.put(box_crossValidation, new CrossValidationBox());
			boxes.put(box_visualizer, new VisualizerBox());
			boxes.put(box_differenceVisualizer, new DifferenceVisualizerBox());
		}
	}
	
	public void exportXML() throws IOException
	{
		for (Box boxI : boxes.values())
		{
			File fileI = new File("src/" + boxI.getAgentName() + ".xml");
			boxI.exportXML(fileI);
		}
	}
}