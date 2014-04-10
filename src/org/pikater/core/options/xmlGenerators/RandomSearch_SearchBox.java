package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class RandomSearch_SearchBox extends LogicalBoxDescription {

	protected RandomSearch_SearchBox() {
		super("Random-Searcher",
				Search.class,
				"Selects and provides random values for its output parameters."
				);

		this.setPicture("picture1.jpg");
		this.setAgentName(Agent_RandomSearch.class);

		AbstractParameter paramE = new RangedValueParameter<Double>(0.01, new Interval<Double>(0.0, 1.0), false);
		AbstractParameter paramM = new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 100000), false);

		this.addParameter(paramE);
		this.addParameter(paramM);
	}
}