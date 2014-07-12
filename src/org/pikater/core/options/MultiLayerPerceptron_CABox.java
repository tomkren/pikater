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
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.StringValue;


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
		ValueType typeL = new ValueType(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.001f), new FloatValue(1.0f) ));
		TypeRestrictions restrictionL = new TypeRestrictions();
		restrictionL.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeL )) ));
		
		
		NewOption optionL = new NewOption(
				new FloatValue(0.3f),
				new ValueType(FloatValue.class),
				"L" );
		optionL.setDescription("Learning rate");
		optionL.setTypeRestrictions(restrictionL);

		
		/**
		# Momentum Rate for the backpropagation algorithm., Default = 0.2
		$ M float 1 1 r 0 0.9
		**/
		ValueType typeM = new ValueType(FloatValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(0.9f) ));
		TypeRestrictions restrictionM = new TypeRestrictions();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new FloatValue(0.2f),
				new ValueType(FloatValue.class),
				"M" );
		optionM.setDescription("Momentum Rate for the backpropagation algorithm");
		optionM.setTypeRestrictions(restrictionM);

		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 10000
		**/
		ValueType typeN = new ValueType(FloatValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10000) ));
		TypeRestrictions restrictionN = new TypeRestrictions();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of epochs to train through");
		optionN.setTypeRestrictions(restrictionN);

		
		/**
		#  Percentage size of validation set to use to terminate
		#  training (if this is non zero it can pre-empt num of epochs.
		#  (Value should be between 0 - 100, Default = 0).
		$ V int 1 1 r 0 100
		**/
		ValueType typeV = new ValueType(IntegerValue.class);
		typeV.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(100) ));
		TypeRestrictions restrictionV = new TypeRestrictions();
		restrictionV.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeV )) ));
		
		NewOption optionV = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"N" );
		optionV.setDescription("Percentage size of validation");
		optionV.setTypeRestrictions(restrictionV);

		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		ValueType typeS = new ValueType(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		TypeRestrictions restrictionS = new TypeRestrictions();
		restrictionV.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"S" );
		optionS.setDescription("Seed the random number generator");
		optionS.setTypeRestrictions(restrictionS);
		
		
		/**
		#  The consequetive number of errors allowed for validation
		#  testing before the netwrok terminates.
		#  (Value should be > 0, Default = 20).
		$ E int 1 1 r 0 50
		**/
		ValueType typeE = new ValueType(IntegerValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(50) ));
		TypeRestrictions restrictionE = new TypeRestrictions();
		restrictionE.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new IntegerValue(20),
				new ValueType(IntegerValue.class),
				"E" );
		optionE.setDescription("The consequetive number of errors allowed for validation");
		optionE.setTypeRestrictions(restrictionE);

		
		/**
		# Learning rate decay will occur; 0 arguments
		$ D boolean
		**/
		ValueType typeD = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionD = new TypeRestrictions();
		restrictionD.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeD )) ));
		
		NewOption optionD = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"D" );
		optionD.setDescription("Learning rate");
		optionD.setTypeRestrictions(restrictionD);

		
		/**
		# Normalizing the attributes will NOT be done.
		$ I boolean
		**/
		ValueType typeI = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionI = new TypeRestrictions();
		restrictionI.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"I" );
		optionI.setDescription("Normalizing the attributes will NOT be done");
		optionI.setTypeRestrictions(restrictionI);
		
		
		/**
		#  GUI will be opened.
		#  (Use this to bring up a GUI).
		$ G boolean
		**/
		ValueType typeG = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionG = new TypeRestrictions();
		restrictionG.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"G" );
		optionG.setDescription("GUI will be opened");
		optionG.setTypeRestrictions(restrictionG);
		
		
		/**
		# The hidden layers to be created for the network.
		# (Value should be a list of comma seperated Natural numbers or the letters 'a' = (attribs + classes) / 2,
		# 'i' = attribs, 'o' = classes, 't' = attribs .+ classes) For wildcard values,Default = a).
		# type list - length (2 numbers), range (2 numbers)  ... max
		$ H mixed 1 3 s 2, 3, 4, 5, 6, 7, 8, 9, 10, i, o
		**/		
		ValueType typeHn = new ValueType(IntegerValue.class);
		typeHn.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(2), new IntegerValue(10) ));
		List<ITypedValue> list =  new ArrayList<ITypedValue>(
				new ArrayList<ITypedValue>(Arrays.asList(
					new StringValue("a"), new StringValue("i"),
					new StringValue("o"), new StringValue("t")
					)) );		
		ValueType typeHs = new ValueType(StringValue.class);
		typeHs.setSetRestriction(
				new SetRestriction(list) );
				
		TypeRestrictions restrictionH = new TypeRestrictions();
		restrictionH.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeHn, typeHs )) ));
		
		NewOption optionH = new NewOption(
				new IntegerValue(2),
				new ValueType(IntegerValue.class),
				"E" );
		optionH.setDescription("The hidden layers to be created for the network");
		optionH.setTypeRestrictions(restrictionH);
		
		
		/**
		#  A NominalToBinary filter will NOT automatically be used.
		#  (Set this to not use a NominalToBinary filter).
		$ B boolean
        ***/		
		ValueType typeB = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionB = new TypeRestrictions();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"B" );
		optionB.setDescription("A NominalToBinary filter will NOT automatically be used");
		optionB.setTypeRestrictions(restrictionB);
	
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		ValueType typeC = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionC = new TypeRestrictions();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"C" );
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setTypeRestrictions(restrictionC);


		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(ComputingAgent.class);
		agentInfo.setOntologyClass(Agent_WekaPerceptronCA.class);
	
		agentInfo.setName("MultiLayerPerceptron");
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
