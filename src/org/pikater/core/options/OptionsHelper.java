package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		NewOption optModel = new NewOption("model", new NullValue());

		List<IValueData> durationValues = new ArrayList<IValueData>();
		durationValues.add(new StringValue(ShortTimeDuration.class.getSimpleName()));
		durationValues.add(new StringValue(LongTermDuration.class.getSimpleName()));

		NewOption optDuration = new NewOption(
				"duration",
				new StringValue(LongTermDuration.class.getSimpleName()),
				new SetRestriction(false, durationValues));
		
		return Arrays.asList(
				optModel,
				optDuration
		);
	}
}