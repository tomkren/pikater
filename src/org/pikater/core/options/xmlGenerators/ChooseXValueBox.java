package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class ChooseXValueBox extends LogicalUnit {
	
	protected ChooseXValueBox(){
		this.setAgentName("Agent_ChooseXValues");
		this.setDescription("...");
		this.setDisplayName("Choose X Values Agent");
		this.setType(BoxType.SEARCHER);
		this.setIsBox(true);
		this.setPicture("picture3.jpg");
		
		AbstractParameter paramN = new RangedValueParameter<Integer>(
				new Integer(5),
				new Interval<Integer>(1, 2000) ,
				true
				);
		
		this.addParameter(paramN);
	}
}
