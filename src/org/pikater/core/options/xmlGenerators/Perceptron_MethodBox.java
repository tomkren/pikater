package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalBoxDescription;

public class Perceptron_MethodBox extends LogicalBoxDescription {
	public Perceptron_MethodBox(){
		super("Perceptron",Method.class,"Perceptron Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/	
		OptionDefault optionL = new OptionDefault();
		optionL.setName("L");
		optionL.setDescription("Learning rate");
		optionL.setValue(
				new OptionValue(new Float(0.3f)) );
		optionL.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1.0f)) );
		optionL.setList( new OptionList() );
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Number of epochs to train through");
		optionN.setValue(
				new OptionValue(new Integer(1)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(1000)) );
		optionN.setList( new OptionList() );
		
		
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/		
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("Seed of the random number generator");
		optionS.setValue(
				new OptionValue(new Integer(0)) );
		optionS.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		optionS.setList( new OptionList() );
		
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setValue(
				new OptionValue(new Boolean(false)) );
		
		this.addParameter(optionL);
		this.addParameter(optionN);
		this.addParameter(optionS);
		this.addParameter(optionC);
	}
}
