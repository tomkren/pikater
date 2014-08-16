package org.pikater.core.options;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstants;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.LongTermDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ShortTimeDuration;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

public class OptionsHelper
{
	public static List<NewOption> getCAOptions()
	{
		List<NewOption> options = OptionsHelper.getNotSpecifiedCAOptions();
		
		NewOption optModel = new NewOption(CoreConstants.MODEL, new NullValue());

		options.add(optModel);
		
		return options;
	}
	
	public static List<NewOption> getNotSpecifiedCAOptions() {

		List<IValueData> durationValues = new ArrayList<IValueData>();
		durationValues.add(new StringValue(ShortTimeDuration.class.getSimpleName()));
		durationValues.add(new StringValue(LongTermDuration.class.getSimpleName()));

		NewOption optDuration = new NewOption(
				CoreConstants.DURATION,
				new StringValue(LongTermDuration.class.getSimpleName()),
				new SetRestriction(false, durationValues));
		
		List<IValueData> modeValues = new ArrayList<IValueData>();
		modeValues.add(new StringValue(CoreConstants.MODE_TRAIN_ONLY));
		modeValues.add(new StringValue(CoreConstants.MODE_TEST_ONLY));
		modeValues.add(new StringValue(CoreConstants.MODE_TRAIN_TEST));

		NewOption optMode = new NewOption(
				CoreConstants.MODE,
				new StringValue(CoreConstants.MODE_TRAIN_ONLY),
				new SetRestriction(false, modeValues));
		
		
		List<IValueData> outputValues = new ArrayList<IValueData>();
		outputValues.add(new StringValue(CoreConstants.OUTPUT_EVALUATION_ONLY));
		outputValues.add(new StringValue(CoreConstants.OUTPUT_PREDICTION));

		NewOption optOutput = new NewOption(
				CoreConstants.OUTPUT,
				new StringValue(CoreConstants.OUTPUT_PREDICTION),
				new SetRestriction(false, outputValues));

		List<NewOption> options = new ArrayList<NewOption>();
		options.add(optDuration);
		options.add(optMode);
		options.add(optOutput);
		
		return options;
	}
	
}