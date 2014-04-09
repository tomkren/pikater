package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class GASearchBox extends LogicalUnit{
	protected GASearchBox(){
		this.setAgentName("Agent_GASearch");
		this.setDescription("....");
		this.setDisplayName("GASearch");
		this.setType(BoxType.SEARCHER);
		this.setIsBox(true);
		this.setOntology(null);
		this.setPicture("picture3.jpg");
		
		
		RangedValueParameter<Float> parE=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(10, new Interval<Integer>(1,1000), true);
		RangedValueParameter<Float> parT=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Float> parX=new RangedValueParameter<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		RangedValueParameter<Integer> parP=new RangedValueParameter<Integer>(5, new Interval<Integer>(1,100), true);
		RangedValueParameter<Integer> parS=new RangedValueParameter<Integer>(2, new Interval<Integer>(1,100), true);
		
		this.addParameter(parE);
		this.addParameter(parM);
		this.addParameter(parT);
		this.addParameter(parX);
		this.addParameter(parP);
		this.addParameter(parS);
	}
}
