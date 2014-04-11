package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

public class Perceptron_MethodBox extends LogicalBoxDescription {
	public Perceptron_MethodBox(){
		super("Perceptron",Method.class,"Perceptron Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/
		RangedValueParameter<Float> parL=new RangedValueParameter<Float>(
				0.3f,
				new Interval<Float>(0.0f, 1.0f),
				true);
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		RangedValueParameter<Integer> parN=new RangedValueParameter<Integer>(
				1,
				new Interval<Integer>(1, 1000),
				true);
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		RangedValueParameter<Integer> parS=new RangedValueParameter<Integer>(
				0,
				new Interval<Integer>(0, Integer.MAX_VALUE),
				true);
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		ValueParameter<Boolean> parC=new ValueParameter<Boolean>(false);
		
		this.addParameter(parL);
		this.addParameter(parN);
		this.addParameter(parS);
		this.addParameter(parC);
	}
}
