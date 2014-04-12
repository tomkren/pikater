package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.EnumeratedValueParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

public class SMO_MethodBox extends LogicalBoxDescription {
	public SMO_MethodBox(){
		super("SMO",Method.class,"SMO Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		/**
		# -C num The complexity constant C. (default 1)
		$ C float 1 1 r 0.0001 5
		**/
		RangedValueParameter<Float> parC=new RangedValueParameter<Float>(
				1.0f,
				new Interval<Float>(0.0001f, 5.0f),
				true);
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		ValueParameter<Integer> parE=new ValueParameter<Integer>(1);
		/**
		# -G num Gamma for the RBF kernel. (default 0.01)
		$ G float 1 1 r 0.0001 4
		**/
		RangedValueParameter<Float> parG=new RangedValueParameter<Float>(
				0.01f,
				new Interval<Float>(0.0001f, 4.0f),
				true);
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		EnumeratedValueParameter<Integer> parN=new EnumeratedValueParameter<Integer>(
				0,
				new ArrayList<Integer>(Arrays.asList(new Integer[] {0,1,2}))
				);
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		ValueParameter<Boolean> parF=new ValueParameter<Boolean>(false);
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		ValueParameter<Boolean> parO=new ValueParameter<Boolean>(false);
		/**
		# Use the RBF kernel. (default poly)
		$ R boolean
		**/
		ValueParameter<Boolean> parR=new ValueParameter<Boolean>(false);
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/
		RangedValueParameter<Integer> parA=new RangedValueParameter<Integer>(
				250007,
				new Interval<Integer>(0, Integer.MAX_VALUE),
				true);
		/**
		# -L num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		ValueParameter<Float> parL=new ValueParameter<Float>(1.0e-3f);
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		ValueParameter<Float> parP=new ValueParameter<Float>(1.0e-12f);
		/**
		# Fit logistic models to SVM outputs.
		$ M boolean
		**/
		ValueParameter<Boolean> parM=new ValueParameter<Boolean>(false);
		/**
		# -V num
		# Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)
		**/
		ValueParameter<Integer> parV=new ValueParameter<Integer>(-1);
		/**
		# Random number seed for cross-validation. (default 1)
		$ W int 1 1 r 1 MAXINT
		**/
		RangedValueParameter<Integer> parW=new RangedValueParameter<Integer>(
				1,
				new Interval<Integer>(1, Integer.MAX_VALUE),
				true);
		
		this.addParameter(parC);
		this.addParameter(parE);
		this.addParameter(parG);
		this.addParameter(parN);
		this.addParameter(parF);
		this.addParameter(parO);
		this.addParameter(parR);
		this.addParameter(parA);
		this.addParameter(parL);
		this.addParameter(parP);
		this.addParameter(parM);
		this.addParameter(parV);
		this.addParameter(parW);
	}
}
