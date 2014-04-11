package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.ValueParameter;

public class NaiveBayes_MethodBox extends LogicalBoxDescription {
	public NaiveBayes_MethodBox(){
		super("NaiveBayes",Method.class,"Naive Bayes Method");
		this.setPicture("picture.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		ValueParameter<Boolean> parK=new ValueParameter<Boolean>(false);
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		ValueParameter<Boolean> parD=new ValueParameter<Boolean>(false);
		
		this.addParameter(parK);
		this.addParameter(parD);
	}
}
