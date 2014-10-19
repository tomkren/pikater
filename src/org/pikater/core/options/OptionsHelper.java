package org.pikater.core.options;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.batchdescription.duration.ExpectedDuration.DurationType;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.Value;
import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.core.ontology.subtrees.newoption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newoption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newoption.values.NullValue;
import org.pikater.core.ontology.subtrees.newoption.values.StringValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;

/**
 * 
 * Abstract class which helps with the definition of options in the new Box.
 *
 */
public class OptionsHelper {
	
	/**
	 * Get the options for the Computing Agent
	 */
	public static List<NewOption> getCAOptions() {
		List<NewOption> options = OptionsHelper.getNotSpecifiedCAOptions();
		
		Value defaultValue = new Value(new NullValue());
		
		NewOption optModel = new NewOption(CoreConstant.MODEL, defaultValue, 
				defaultValue.getType(),
				new ValueType(new IntegerValue(-1),
						new RangeRestriction(new IntegerValue(-1), null))
		);

		options.add(optModel);
		
		return options;
	}
	
	/**
	 * Get the options for the non-specific Computing Agent
	 */
	public static List<NewOption> getNotSpecifiedCAOptions() {

		List<IValueData> durationValues = new ArrayList<IValueData>();
		for (DurationType durationTypeI :
			DurationType.values()) {
			
			String guiValueI = durationTypeI.getGuiValue();
			durationValues.add(new StringValue(guiValueI));
		}

		NewOption optDuration = new NewOption(
				CoreConstant.DURATION,
				new StringValue(DurationType.MINUTES.getGuiValue()),
				new SetRestriction(false, durationValues));
		
		List<IValueData> modeValues = new ArrayList<IValueData>();
		modeValues.add(
				new StringValue(CoreConstant.Mode.TRAIN_ONLY.name()));
		modeValues.add(
				new StringValue(CoreConstant.Mode.TEST_ONLY.name()));
		modeValues.add(
				new StringValue(CoreConstant.Mode.TRAIN_TEST.name()));

		NewOption optMode = new NewOption(
				CoreConstant.Mode.DEFAULT.name(),
				new StringValue(CoreConstant.Mode.TRAIN_ONLY.name()),
				new SetRestriction(false, modeValues));
		
		
		List<IValueData> outputValues = new ArrayList<IValueData>();
		outputValues.add(
				new StringValue(CoreConstant.Output.EVALUATION_ONLY.name()));
		outputValues.add(
				new StringValue(CoreConstant.Output.PREDICTION.name()));

		NewOption optOutput = new NewOption(
				CoreConstant.Output.DEFAULT.name(),
				new StringValue(CoreConstant.Output.PREDICTION.name()),
				new SetRestriction(false, outputValues));

		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optDuration);
		options.add(optMode);
		options.add(optOutput);
		
		return options;
	}
	
}
