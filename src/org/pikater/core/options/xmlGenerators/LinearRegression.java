package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

public class LinearRegression extends LogicalUnitDescription
{	
	
	public LinearRegression() {

		//"Linear regression"
		this.setOntology(ComputingAgent.class);
		
		AbstractParameter paramS = new RangedValueParameter<Integer>(
				0,
				new Interval<Integer>(0, 2),
				false);
		
		ValueParameter<Boolean> paramC = new ValueParameter<Boolean>(true);
		
		AbstractParameter paramR = new RangedValueParameter<Float>(
				0.00000001f,
				new Interval<Float>(0.0000000001f, 0.0001f),
				false);
		
		this.addParameter(paramS);
		this.addParameter(paramC);
		this.addParameter(paramR);
	}

}
