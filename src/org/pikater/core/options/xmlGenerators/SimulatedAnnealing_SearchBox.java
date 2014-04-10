package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class SimulatedAnnealing_SearchBox extends LogicalBoxDescription {

	protected SimulatedAnnealing_SearchBox() {
		super("SimulatedAnnealing-Searcher",
				Search.class,
				"Searches defined parameters and provides them in output slots. Simulated annealing is used for searching."
				);

		this.setPicture("picture2.jpg");
		this.setAgentName(Agent_SimulatedAnnealing.class);
		
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