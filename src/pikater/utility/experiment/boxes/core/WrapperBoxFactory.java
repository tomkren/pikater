package pikater.utility.experiment.boxes.core;

import pikater.utility.experiment.boxes.specific.InputBox;
import pikater.utility.experiment.boxes.specific.RandomSearchBox;
import pikater.utility.experiment.boxes.specific.VisualizerBox;

public class WrapperBoxFactory
{
	public static WrapperBox getInputSearcherVisualizer()
	{
		InputBox input = new InputBox();
		RandomSearchBox searcher = new RandomSearchBox();
		VisualizerBox visualizer = new VisualizerBox();
		
		WrapperBox result = new WrapperBox("InputSearcherVisualizer", "picture-NAV", null, input, searcher, visualizer);
		
		// TODO: connect the boxes somehow?
		
		return result;
	}
}
