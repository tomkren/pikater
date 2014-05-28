package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.value.StringValue;


public class MultiLayerPerceptron_CABox {
	
	public static AgentInfo get() {

		/**
		# name, type, number od values, parameters range / set
		# r ... range
		# s ... set  (example: s 1, 2, 3, 4, 5, 6, 7, 8)
		# 

		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0.001 1
		**/
		Type typeL = new Type(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.001f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionL = new PossibleTypesRestriction();
		restrictionL.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeL )) ));
		
		
		NewOption optionL = new NewOption(
				new FloatValue(0.3f),
				new Type(FloatValue.class),
				"L" );
		optionL.setDescription("Learning rate");
		optionL.setPossibleTypesRestriction(restrictionL);

		
		/**
		# Momentum Rate for the backpropagation algorithm., Default = 0.2
		$ M float 1 1 r 0 0.9
		**/
		Type typeM = new Type(FloatValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(0.9f) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new FloatValue(0.2f),
				new Type(FloatValue.class),
				"M" );
		optionM.setDescription("Momentum Rate for the backpropagation algorithm");
		optionM.setPossibleTypesRestriction(restrictionM);

		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 10000
		**/
		Type typeN = new Type(FloatValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10000) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of epochs to train through");
		optionN.setPossibleTypesRestriction(restrictionN);

		
		/**
		#  Percentage size of validation set to use to terminate
		#  training (if this is non zero it can pre-empt num of epochs.
		#  (Value should be between 0 - 100, Default = 0).
		$ V int 1 1 r 0 100
		**/
		Type typeV = new Type(IntegerValue.class);
		typeV.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(100) ));
		PossibleTypesRestriction restrictionV = new PossibleTypesRestriction();
		restrictionV.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeV )) ));
		
		NewOption optionV = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"N" );
		optionV.setDescription("Percentage size of validation");
		optionV.setPossibleTypesRestriction(restrictionV);

		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		Type typeS = new Type(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionS = new PossibleTypesRestriction();
		restrictionV.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"S" );
		optionS.setDescription("Seed the random number generator");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		
		/**
		#  The consequetive number of errors allowed for validation
		#  testing before the netwrok terminates.
		#  (Value should be > 0, Default = 20).
		$ E int 1 1 r 0 50
		**/
		Type typeE = new Type(IntegerValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(50) ));
		PossibleTypesRestriction restrictionE = new PossibleTypesRestriction();
		restrictionE.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new IntegerValue(20),
				new Type(IntegerValue.class),
				"E" );
		optionE.setDescription("The consequetive number of errors allowed for validation");
		optionE.setPossibleTypesRestriction(restrictionE);

		
		/**
		# Learning rate decay will occur; 0 arguments
		$ D boolean
		**/
		Type typeD = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionD = new PossibleTypesRestriction();
		restrictionD.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeD )) ));
		
		NewOption optionD = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"D" );
		optionD.setDescription("Learning rate");
		optionD.setPossibleTypesRestriction(restrictionD);

		
		/**
		# Normalizing the attributes will NOT be done.
		$ I boolean
		**/
		Type typeI = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionI = new PossibleTypesRestriction();
		restrictionI.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"I" );
		optionI.setDescription("Normalizing the attributes will NOT be done");
		optionI.setPossibleTypesRestriction(restrictionI);
		
		
		/**
		#  GUI will be opened.
		#  (Use this to bring up a GUI).
		$ G boolean
		**/
		Type typeG = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionG = new PossibleTypesRestriction();
		restrictionG.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"G" );
		optionG.setDescription("GUI will be opened");
		optionG.setPossibleTypesRestriction(restrictionG);
		
		
		/**
		# The hidden layers to be created for the network.
		# (Value should be a list of comma seperated Natural numbers or the letters 'a' = (attribs + classes) / 2,
		# 'i' = attribs, 'o' = classes, 't' = attribs .+ classes) For wildcard values,Default = a).
		# type list - length (2 numbers), range (2 numbers)  ... max
		$ H mixed 1 3 s 2, 3, 4, 5, 6, 7, 8, 9, 10, i, o
		**/		
		Type typeHn = new Type(IntegerValue.class);
		typeHn.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(2), new IntegerValue(10) ));
		List<IValue> list =  new ArrayList<IValue>(
				new ArrayList<IValue>(Arrays.asList(
					new StringValue("a"), new StringValue("i"),
					new StringValue("o"), new StringValue("t")
					)) );		
		Type typeHs = new Type(StringValue.class);
		typeHs.setSetRestriction(
				new SetRestriction(list) );
				
		PossibleTypesRestriction restrictionH = new PossibleTypesRestriction();
		restrictionH.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeHn, typeHs )) ));
		
		NewOption optionH = new NewOption(
				new IntegerValue(2),
				new Type(IntegerValue.class),
				"E" );
		optionH.setDescription("The hidden layers to be created for the network");
		optionH.setPossibleTypesRestriction(restrictionH);
		
		
		/**
		#  A NominalToBinary filter will NOT automatically be used.
		#  (Set this to not use a NominalToBinary filter).
		$ B boolean
        ***/		
		Type typeB = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"B" );
		optionB.setDescription("A NominalToBinary filter will NOT automatically be used");
		optionB.setPossibleTypesRestriction(restrictionB);
	
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		Type typeC = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"C" );
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setPossibleTypesRestriction(restrictionC);


		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(ComputingAgent.class);
		agentInfo.setOntologyClass(Agent_WekaPerceptronCA.class);
	
		agentInfo.setName("MultiLayerPerceptron");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Multi-layer perceptron method");

		agentInfo.addOption(optionC);
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionG);
		agentInfo.addOption(optionI);
		agentInfo.addOption(optionD);
		agentInfo.addOption(optionE);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionV);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionL);
		agentInfo.addOption(optionH);


		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}
}
