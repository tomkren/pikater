package org.pikater.core.options;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ExpectedDuration.DurationType;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class OptionsHelper
{
	public static List<NewOption> getCAOptions()
	{
		List<NewOption> options = OptionsHelper.getNotSpecifiedCAOptions();
		
		Value defaultValue = new Value(new NullValue());
		
		NewOption optModel = new NewOption(CoreConstant.Misc.MODEL.get(), defaultValue, 
				defaultValue.getType(),
				new ValueType(new IntegerValue(-1), // WEB requires -1
						new RangeRestriction(new IntegerValue(-1), null)) // WEB requires -1
		);

		options.add(optModel);
		
		return options;
	}
	
	public static List<NewOption> getNotSpecifiedCAOptions() {

		List<IValueData> durationValues = new ArrayList<IValueData>();
		for (DurationType durationTypeI :
			DurationType.values()) {
			
			String guiValueI = durationTypeI.getGuiValue();
			durationValues.add(new StringValue(guiValueI));
		}

		NewOption optDuration = new NewOption(
				CoreConstant.Misc.DURATION.get(),
				new StringValue(DurationType.MINUTES.getGuiValue()),
				new SetRestriction(false, durationValues));
		
		List<IValueData> modeValues = new ArrayList<IValueData>();
		modeValues.add(new StringValue(CoreConstant.Mode.TRAIN_ONLY.get()));
		modeValues.add(new StringValue(CoreConstant.Mode.TEST_ONLY.get()));
		modeValues.add(new StringValue(CoreConstant.Mode.TRAIN_TEST.get()));

		NewOption optMode = new NewOption(
				CoreConstant.Mode.DEFAULT.get(),
				new StringValue(CoreConstant.Mode.TRAIN_ONLY.get()),
				new SetRestriction(false, modeValues));
		
		
		List<IValueData> outputValues = new ArrayList<IValueData>();
		outputValues.add(new StringValue(CoreConstant.Output.EVALUATION_ONLY.get()));
		outputValues.add(new StringValue(CoreConstant.Output.PREDICTION.get()));

		NewOption optOutput = new NewOption(
				CoreConstant.Output.DEFAULT.get(),
				new StringValue(CoreConstant.Output.PREDICTION.get()),
				new SetRestriction(false, outputValues));

		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optDuration);
		options.add(optMode);
		options.add(optOutput);
		
		return options;
	}
	
}