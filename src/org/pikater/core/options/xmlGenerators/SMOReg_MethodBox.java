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

public class SMOReg_MethodBox extends LogicalBoxDescription {
	public SMOReg_MethodBox(){
		super("SMO Reg",Method.class,"SMOReg Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		/**
		# -S num
		# The amount up to which deviation are tolerated (epsilon). (default 1e-3)
		# Watch out, the value of epsilon is used with the (normalized/standardize) data
		**/
		ValueParameter<Float> parS=new ValueParameter<Float>(1e-3f);
		/**
		# -C num
		# The complexity constant C. (default 1)
		**/
		ValueParameter<Integer> parC=new ValueParameter<Integer>(1);
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		ValueParameter<Integer> parE=new ValueParameter<Integer>(1);
		/**
		# -G num
		# Gamma for the RBF kernel. (default 0.01)
		**/
		ValueParameter<Float> parG=new ValueParameter<Float>(0.01f);
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
		# Use RBF kernel (default poly).
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
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		ValueParameter<Float> parP=new ValueParameter<Float>(1.0e-12f);
		/**
		# -T num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		ValueParameter<Float> parT=new ValueParameter<Float>(1.0e-3f);
		
		this.addParameter(parS);
		this.addParameter(parC);
		this.addParameter(parE);
		this.addParameter(parG);
		this.addParameter(parN);
		this.addParameter(parF);
		this.addParameter(parO);
		this.addParameter(parR);
		this.addParameter(parA);
		this.addParameter(parP);
		this.addParameter(parT);
	}
}
