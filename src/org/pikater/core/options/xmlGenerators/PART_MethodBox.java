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

public class PART_MethodBox extends LogicalBoxDescription {
	public PART_MethodBox(){
		super("PART",Method.class,"PART Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# Set confidence threshold for pruning. (Default: 0.25)
		# $ C float 1 1 r 0.0001 0.4 
		$ C float 1 1 s null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4, 0.5
		**/
		EnumeratedValueParameter<Float> parC=new EnumeratedValueParameter<Float>(
				0.25f,
				new ArrayList<Float>(Arrays.asList(new Float[] {null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f,0.5f}))
				);
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		RangedValueParameter<Integer> parM=new RangedValueParameter<Integer>(
				2,
				new Interval<Integer>(1, 10),
				true);
		/**
		# Use reduced error pruning.
		$ R boolean
		**/
		ValueParameter<Boolean> parR=new ValueParameter<Boolean>(false);
		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		EnumeratedValueParameter<Integer> parN=new EnumeratedValueParameter<Integer>(
				3,
				new ArrayList<Integer>(Arrays.asList(new Integer[] {null,1,2,3,4,5}))
				);
		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		ValueParameter<Boolean> parB=new ValueParameter<Boolean>(false);
		/**
		# Generate unpruned decision list.
		$ U boolean
		**/
		ValueParameter<Boolean> parU=new ValueParameter<Boolean>(false);
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		RangedValueParameter<Integer> parQ=new RangedValueParameter<Integer>(
				1,
				new Interval<Integer>(1, Integer.MAX_VALUE),
				true);
		
		
		this.addParameter(parC);
		this.addParameter(parM);
		this.addParameter(parR);
		this.addParameter(parN);
		this.addParameter(parB);
		this.addParameter(parU);
		this.addParameter(parQ);
		
		
	}
}
