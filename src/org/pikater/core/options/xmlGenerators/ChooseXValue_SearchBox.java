package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class ChooseXValue_SearchBox extends LogicalBoxDescription {
	
	protected ChooseXValue_SearchBox() {
		super(
			"Choose X Values Agent",
		        Search.class,
		        "Search which Choose X Values"
		        );

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_ChooseXValues.class);
		
		AbstractParameter paramN = new RangedValueParameter<Integer>(
				new Integer(5),
				new Interval<Integer>(1, 2000) ,
				true
				);
		
		this.addParameter(paramN);
	}
}
