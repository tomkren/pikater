package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class RBFNetwork_MethodBox extends LogicalBoxDescription {
	public RBFNetwork_MethodBox(){
		super("RBFNetwork",Method.class,"RBFNetwork Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		/**
		# number of clusters, default 2
		$ B int 1 1 r 2 1000
		**/
		RangedValueParameter<Integer> parB=new RangedValueParameter<Integer>(
				2,
				new Interval<Integer>(2, 1000),
				true);
		/**
		# minStdDev, default 0.1
		$ W float 1 1 r 0.01 2
		**/
		RangedValueParameter<Float> parW=new RangedValueParameter<Float>(
				0.1f,
				new Interval<Float>(0.01f, 2.0f),
				true);
		/**
		# Ridge (Set the Ridge value for the logistic or linear regression), default 1.0E-8
		$ R float 1 1 r 0.000000001 10
		**/
		RangedValueParameter<Float> parR=new RangedValueParameter<Float>(
				1.0e-8f,
				new Interval<Float>(0.000000001f, 10.0f),
				true);
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		RangedValueParameter<Integer> parS=new RangedValueParameter<Integer>(
				0,
				new Interval<Integer>(0, Integer.MAX_VALUE),
				true);
		/**
		#  Set the maximum number of iterations for the logistic regression. (default -1, until convergence).
		$ M int 1 1 r -1 50
		**/		
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(
				-1,
				new Interval<Integer>(-1, 50),
				true);
		
		this.addParameter(parB);
		this.addParameter(parW);
		this.addParameter(parR);
		this.addParameter(parS);
		this.addParameter(parM);
	}
}
