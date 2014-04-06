package org.options.xmlGenerators;

import org.options.LogicalUnit;
import org.shared.boxTypes.TypesOfBox;
import org.shared.interval.Interval;
import org.shared.parameters.AbstractParameter;
import org.shared.parameters.RangedValueParameter;


public class RandomSearchBox extends LogicalUnit {
	
	protected RandomSearchBox() {
		
		this.setIsBox(true);
		this.setName("Random-Searcher");
		this.setAgentName("pikater/Agent_RandomSearch");
		this.setType(TypesOfBox.SEARCHER_BOX);
		this.setOntology(null);
		this.setPicture("picture1.jpg");
		this.setDescription("Selects and provides random values for its output parameters.");


		AbstractParameter paramE = new RangedValueParameter<Double>(
				0.01,
				new Interval<Double>(0.0, 1.0),
				false
				);

		AbstractParameter paramM = new RangedValueParameter<Integer>(
				10,
				new Interval<Integer>(1, 100000),
				false
				);

		this.addParameter(paramE);
		this.addParameter(paramM);

	}
	
}
