package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class GridSearchBox extends LogicalUnit {
	protected GridSearchBox(){
		this.setAgentName("Agent_GridSearch");
		this.setDescription("....");
		this.setDisplayName("GridSearch");
		this.setType(BoxType.SEARCHER);
		this.setIsBox(true);
		this.setOntology(null);
		this.setPicture("picture3.jpg");
		
		RangedValueParameter<Integer> parB=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,100000), true);
		RangedValueParameter<Integer> parN=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,1000), true);
		RangedValueParameter<Float> parZ=new RangedValueParameter<Float>(0.000000001f, new Interval<Float>(0.0f, 1000.0f), true);
		
		this.addParameter(parB);
		this.addParameter(parN);
		this.addParameter(parZ);
	}
}
