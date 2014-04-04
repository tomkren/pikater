package pikater.utility.experiment.boxes.specific;

import java.util.EnumSet;
import java.util.Set;

import pikater.utility.experiment.boxes.core.LeafBox;
import pikater.utility.experiment.parameters.RangedValueParameter;
import pikater.utility.experiment.parameters.info.CrossValidationBoxParams;
import pikater.utility.experiment.slots.DataSlot;
import pikater.utility.experiment.util.Interval;

public class CrossValidationBox extends LeafBox
{
	private final Set<LeafBoxCategory> categories;
	
	public CrossValidationBox()
	{
		super("CrossValidation-Method", "picture3.jpg", "Computing agent used for training neural networks deterministic library WEKA. As a training method is used default "
				+ "Cross Validation WEKA method.", "Agent_CrossValidation", "ComputingAgent");
		
		this.categories = EnumSet.of(LeafBoxCategory.COMPUTING);
		
		addEditableParameter(CrossValidationBoxParams.f, new RangedValueParameter<Integer>(
				5,
				new Interval<Integer>(1, 100),
				false)
		);

		addInputSlot(new DataSlot("Training data"));
		addInputSlot(new DataSlot("Testing data"));
		addInputSlot(new DataSlot("Validation data"));
		
		addOutputSlot(new DataSlot("Computed data"));
	}

	@Override
	public Set<LeafBoxCategory> getCategories()
	{
		return categories;
	}
}
