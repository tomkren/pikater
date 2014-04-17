package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;

public class NaiveBayes_MethodBox extends LogicalBoxDescription {
	public NaiveBayes_MethodBox(){
		super("NaiveBayes",Method.class,"Naive Bayes Method");
		this.setPicture("picture.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		OptionDefault optionK = new OptionDefault();
		optionK.setName("K");
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		optionK.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		OptionDefault optionD = new OptionDefault();
		optionD.setName("D");
		optionD.setDescription("Use supervised discretization to process numeric attributes");
		optionD.setValue(
				new OptionValue(new Boolean(false)) );
		
		this.addParameter(optionK);
		this.addParameter(optionD);
	}
}
