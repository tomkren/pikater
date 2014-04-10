package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class GridSearch_SearchBox extends LogicalBoxDescription {
	protected GridSearch_SearchBox() {
		super("GridSearch",
				Search.class,
				"GridSearch Description"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_GridSearch.class);
		
		RangedValueParameter<Integer> parB=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,100000), true);
		RangedValueParameter<Integer> parN=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,1000), true);
		RangedValueParameter<Float> parZ=new RangedValueParameter<Float>(0.000000001f, new Interval<Float>(0.0f, 1000.0f), true);
		
		this.addParameter(parB);
		this.addParameter(parN);
		this.addParameter(parZ);
	}
}
