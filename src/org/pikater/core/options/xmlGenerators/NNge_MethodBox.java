package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class NNge_MethodBox extends LogicalBoxDescription {
	public NNge_MethodBox(){
		super("NNge",Method.class,"NNge Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# Set the number of folder to use in the computing of the mutual information (default 5)
		$ I int 1 1 r 1 100 
		**/
		RangedValueParameter<Integer> parI=new RangedValueParameter<Integer>(
				5,
				new Interval<Integer>(1, 100),
				true);
		/**
		# Set the number of attempts of generalisation (default 5)
		$ G int 1 1 r 1 50
		**/
		RangedValueParameter<Integer> parG=new RangedValueParameter<Integer>(
				5,
				new Interval<Integer>(1, 50),
				true);
		
		this.addParameter(parI);
		this.addParameter(parG);
	}
}
