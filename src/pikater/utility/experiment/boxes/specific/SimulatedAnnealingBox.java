package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.parameters.RangedValueParameter;
import pikater.utility.experiment.parameters.info.SimulatedAnnealingBoxParams;
import pikater.utility.experiment.slots.ParameterSlot;
import pikater.utility.experiment.util.Interval;

public class SimulatedAnnealingBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;
	
	public SimulatedAnnealingBox()
	{
		super("SimulatedAnnealing-Searcher", "picture2.jpg", "Searcher is using to find values of parameters for computing agents. For search "
				+ "the solution is used method simulated annaling.", "pikater/Agent_SimulatedAnnealing", null);
		
		this.categories = EnumSet.of(LeafBoxCategory.SEARCHER);
		
		addEditableParameter(SimulatedAnnealingBoxParams.e, new RangedValueParameter<Double>(
				0.1,
				new Interval<Double>(0.0, 1.0),
				false)
		);
		addEditableParameter(SimulatedAnnealingBoxParams.m, new RangedValueParameter<Integer>(
				50,
				new Interval<Integer>(1, 1000),
				false)
		);
		addEditableParameter(SimulatedAnnealingBoxParams.t, new RangedValueParameter<Double>(
				1.0,
				new Interval<Double>(0.0, 100.0),
				false)
		);
		addEditableParameter(SimulatedAnnealingBoxParams.s, new RangedValueParameter<Double>(
				0.5,
				new Interval<Double>(0.0, 1.0),
				false)
		);

		// TODO: remove the error
		// addOutputSlot(new ParameterSlot("Found parameters"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
