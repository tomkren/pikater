package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class RandomTree_MethodBox extends LogicalBoxDescription {
	public RandomTree_MethodBox(){
		super("RandomTree",Method.class,"Random Tree Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		RangedValueParameter<Integer> parK=new RangedValueParameter<Integer>(
				1,
				new Interval<Integer>(1, 50),
				true);
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(
				0,
				new Interval<Integer>(0, 100),
				true);
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		RangedValueParameter<Integer> parQ=new RangedValueParameter<Integer>(
				1,
				new Interval<Integer>(1, Integer.MAX_VALUE),
				true);

		this.addParameter(parK);
		this.addParameter(parM);
		this.addParameter(parQ);
	}
}
