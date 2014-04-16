package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalBoxDescription;

public class SMO_MethodBox extends LogicalBoxDescription {
	public SMO_MethodBox(){
		super("SMO",Method.class,"SMO Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		/**
		# -C num The complexity constant C. (default 1)
		$ C float 1 1 r 0.0001 5
		**/		
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Num The complexity");
		optionC.setValue(
				new OptionValue(new Float(1.0f)) );
		optionC.setInterval(
				new OptionInterval(new Float(0.0001f), new Float(5.0f)) );
		optionC.setList( new OptionList() );
		
		
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("The exponent for the polynomial kernel");
		optionE.setValue(
				new OptionValue(new Integer(1)) );
	
		
		
		/**
		# -G num Gamma for the RBF kernel. (default 0.01)
		$ G float 1 1 r 0.0001 4
		**/
		OptionDefault optionG = new OptionDefault();
		optionG.setName("G");
		optionG.setDescription("Num Gamma for the RBF kernel");
		optionG.setValue(
				new OptionValue(new Float(0.01f)) );
		optionG.setInterval(
				new OptionInterval(new Float(0.0001f), new Float(4.0f)) );
		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Num Gamma for the RBF kernel");
		optionN.setValue(
				new OptionValue(new Integer(0)) );
		OptionList listN = new OptionList();
		listN.setList(
				new ArrayList<Object>(Arrays.asList(new Integer[] {0,1,2}))
				);
		optionN.setList(listN);
				
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/		
		OptionDefault optionF = new OptionDefault();
		optionF.setName("F");
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		optionF.setValue(
				new OptionValue(new Boolean(false)) );

		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		OptionDefault optionO = new OptionDefault();
		optionO.setName("O");
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		optionO.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# Use the RBF kernel. (default poly)
		$ R boolean
		**/
		OptionDefault optionR = new OptionDefault();
		optionR.setName("O");
		optionR.setDescription("Use the RBF kernel");
		optionR.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/		
		OptionDefault optionA = new OptionDefault();
		optionA.setName("A");
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		optionA.setValue( new OptionValue(new Integer(250007)) );
		optionA.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		
		/**
		# -L num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/		
		OptionDefault optionL = new OptionDefault();
		optionL.setName("L");
		optionL.setDescription("Sets the tolerance parameter");
		optionL.setValue(
				new OptionValue(new Float(1.0e-3f)) );
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		OptionDefault optionP = new OptionDefault();
		optionP.setName("P");
		optionP.setDescription("Sets the epsilon for round-off error");
		optionP.setValue(
				new OptionValue(new Float(1.0e-12f)) );
		
		/**
		# Fit logistic models to SVM outputs.
		$ M boolean
		**/
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Fit logistic models to SVM outputs");
		optionM.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# -V num
		# Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)
		**/
		OptionDefault optionV = new OptionDefault();
		optionV.setName("V");
		optionV.setDescription("Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)");
		optionV.setValue(
				new OptionValue(new Integer(-1)) );
		
		/**
		# Random number seed for cross-validation. (default 1)
		$ W int 1 1 r 1 MAXINT
		**/
		OptionDefault optionW = new OptionDefault();
		optionW.setName("W");
		optionW.setDescription("Random number seed for cross-validation");
		optionW.setValue( new OptionValue(new Integer(1)) );
		optionW.setInterval(
				new OptionInterval(new Integer(1), new Integer(Integer.MAX_VALUE)) );
		
		
		this.addParameter(optionC);
		this.addParameter(optionE);
		this.addParameter(optionG);
		this.addParameter(optionN);
		this.addParameter(optionF);
		this.addParameter(optionO);
		this.addParameter(optionR);
		this.addParameter(optionA);
		this.addParameter(optionL);
		this.addParameter(optionP);
		this.addParameter(optionM);
		this.addParameter(optionV);
		this.addParameter(optionW);
	}
}
