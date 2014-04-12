package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.ontology.description.Method;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

public class MultiLayerPerceptron_MethodBox extends LogicalBoxDescription {
	public MultiLayerPerceptron_MethodBox(){
		super("MultiLayerPerceptron",Method.class,"Multi-layer perceptron method");
		
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# name, type, number od values, parameters range / set
		# r ... range
		# s ... set  (example: s 1, 2, 3, 4, 5, 6, 7, 8)
		# 

		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0.001 1
		**/
		RangedValueParameter<Float> parL=new RangedValueParameter<Float>(
				0.3f, 
				new Interval<Float>(0.001f, 1.0f),
				true
				);
		
		/**
		# Momentum Rate for the backpropagation algorithm., Default = 0.2
		$ M float 1 1 r 0 0.9
		**/
		RangedValueParameter<Float> parM=new RangedValueParameter<Float>(
				0.2f, 
				new Interval<Float>(0.0f, 0.9f),
				true
				);
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 10000
		**/
		RangedValueParameter<Integer> parN=new RangedValueParameter<Integer>(
				1, 
				new Interval<Integer>(1, 10000),
				true
				);
		/**
		#  Percentage size of validation set to use to terminate
		#  training (if this is non zero it can pre-empt num of epochs.
		#  (Value should be between 0 - 100, Default = 0).
		$ V int 1 1 r 0 100
		**/
		RangedValueParameter<Integer> parV=new RangedValueParameter<Integer>(
				0, 
				new Interval<Integer>(0, 100),
				true
				);
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		RangedValueParameter<Integer> parS=new RangedValueParameter<Integer>(
				0, 
				new Interval<Integer>(0, Integer.MAX_VALUE),
				true
				);
		/**
		#  The consequetive number of errors allowed for validation
		#  testing before the netwrok terminates.
		#  (Value should be > 0, Default = 20).
		$ E int 1 1 r 0 50
		**/
		RangedValueParameter<Integer> parE=new RangedValueParameter<Integer>(
				20, 
				new Interval<Integer>(0, 50),
				true
				);
		/**
		# Learning rate decay will occur; 0 arguments
		$ D boolean
		**/
		ValueParameter<Boolean> parD=new ValueParameter<Boolean>(false);
		/**
		# Normalizing the attributes will NOT be done.
		$ I boolean
		**/
		ValueParameter<Boolean> parI=new ValueParameter<Boolean>(false);
		/**
		#  GUI will be opened.
		#  (Use this to bring up a GUI).
		$ G boolean
		**/
		ValueParameter<Boolean> parG=new ValueParameter<Boolean>(false);
		/**
		# The hidden layers to be created for the network.
		# (Value should be a list of comma seperated Natural numbers or the letters 'a' = (attribs + classes) / 2,
		# 'i' = attribs, 'o' = classes, 't' = attribs .+ classes) For wildcard values,Default = a).
		# type list - length (2 numbers), range (2 numbers)  ... max
		$ H mixed 1 3 s 2, 3, 4, 5, 6, 7, 8, 9, 10, i, o
		**/		
		ValueParameter<String> parH=new ValueParameter<String>("2, 3, 4, 5, 6, 7, 8, 9, 10, i, o");
		/**
		#  A NominalToBinary filter will NOT automatically be used.
		#  (Set this to not use a NominalToBinary filter).
		$ B boolean
        ***/
		ValueParameter<Boolean> parB=new ValueParameter<Boolean>(false);
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		ValueParameter<Boolean> parC=new ValueParameter<Boolean>(false);
		
		
		this.addParameter(parC);
		this.addParameter(parB);
		this.addParameter(parG);
		this.addParameter(parI);
		this.addParameter(parD);
		this.addParameter(parE);
		this.addParameter(parS);
		this.addParameter(parV);
		this.addParameter(parN);
		this.addParameter(parM);
		this.addParameter(parL);
		this.addParameter(parH);
	}
}
