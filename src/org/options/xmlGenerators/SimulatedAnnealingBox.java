package org.options.xmlGenerators;

import org.options.LogicalUnit;
import org.shared.boxTypes.TypesOfBox;
import org.shared.interval.Interval;
import org.shared.parameters.AbstractParameter;
import org.shared.parameters.RangedValueParameter;


public class SimulatedAnnealingBox extends LogicalUnit {

	protected SimulatedAnnealingBox() {

		this.setIsBox(true);
		this.setName("SimulatedAnnealing-Searcher");
		this.setAgentName("pikater/Agent_SimulatedAnnealing");
		this.setType(TypesOfBox.SEARCHER_BOX);
		this.setOntology(null);
		this.setPicture("picture2.jpg");
		this.setDescription("Searcher is using to find values of parameters for computing agents. For search the solution is used method simulated annaling.");
		
		AbstractParameter paramE = new RangedValueParameter<Double>(
				0.1,
				new Interval<Double>(0.0, 1.0),
				false);
		
		AbstractParameter paramM = new RangedValueParameter<Integer>(
				50,
				new Interval<Integer>(1, 1000),
				false);
		
		AbstractParameter paramT = new RangedValueParameter<Double>(
				1.0,
				new Interval<Double>(0.0, 100.0),
				false);
		
		AbstractParameter paramS = new RangedValueParameter<Double>(
				0.5,
				new Interval<Double>(0.0, 1.0),
				false);

		this.addParameter(paramE);
		this.addParameter(paramM);
		this.addParameter(paramT);
		this.addParameter(paramS);
		
	}
}