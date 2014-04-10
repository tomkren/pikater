package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_EASearch;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class EASearch_SearchBox extends LogicalBoxDescription {
	
	protected EASearch_SearchBox() {
		super("Agent_EASearch",
				Search.class,
				"Searcher using Evolution algorithm"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_EASearch.class);
		

		RangedValueParameter<Float> parE=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 1000), true);
		RangedValueParameter<Float> parT=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Float> parX=new RangedValueParameter<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Integer> parP=new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 100), true);
		RangedValueParameter<Integer> parI=new RangedValueParameter<Integer>(100, new Interval<Integer>(1,100000), true);
		RangedValueParameter<Float> parF=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Float> parL=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		
		this.addParameter(parE);
		this.addParameter(parM);
		this.addParameter(parT);
		this.addParameter(parX);
		this.addParameter(parP);
		this.addParameter(parI);
		this.addParameter(parF);
		this.addParameter(parL);
	}
}
