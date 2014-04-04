package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.boxes.core.LeafBox.LeafBoxCategory;
import pikater.utility.experiment.parameters.RangedValueParameter;
import pikater.utility.experiment.parameters.info.RandomSearchBoxParams;
import pikater.utility.experiment.slots.ParameterSlot;
import pikater.utility.experiment.util.Interval;

public class RandomSearchBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;

	public RandomSearchBox()
	{
		super("Random-Searcher", "picture1.jpg", "Selects and provides random values for its output parameters.", "pikater/Agent_RandomSearch", "Search");
		
		this.categories = EnumSet.of(LeafBoxCategory.SEARCHER);
		
		addEditableParameter(RandomSearchBoxParams.e, new RangedValueParameter<Double>(
				0.01,
				new Interval<Double>(0.0, 1.0),
				false)
		);
		addEditableParameter(RandomSearchBoxParams.m, new RangedValueParameter<Integer>(
				10,
				new Interval<Integer>(1, 100000),
				false)
		);

		// TODO: remove the error
		// addInputSlot(new ParameterSlot("Found parameters"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
