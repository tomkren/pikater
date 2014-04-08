package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class RandomSearchBox extends LogicalUnit
{
	protected RandomSearchBox()
	{
		this.setIsBox(true);
		this.setDisplayName("Random-Searcher");
		this.setAgentName("pikater/Agent_RandomSearch");
		this.setType(BoxType.SEARCHER);
		this.setOntology(null);
		this.setPicture("picture1.jpg");
		this.setDescription("Selects and provides random values for its output parameters.");

		AbstractParameter paramE = new RangedValueParameter<Double>(0.01, new Interval<Double>(0.0, 1.0), false);
		AbstractParameter paramM = new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 100000), false);

		this.addParameter(paramE);
		this.addParameter(paramM);
	}
}