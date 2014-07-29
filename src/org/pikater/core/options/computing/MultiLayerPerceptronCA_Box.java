package org.pikater.core.options.computing;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;


public class MultiLayerPerceptronCA_Box {
	
	public static AgentInfo get() {

		/**
		# name, type, number of values, parameters range / set
		# r ... range
		# s ... set  (example: s 1, 2, 3, 4, 5, 6, 7, 8)
		# 

		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0.001 1
		**/
		NewOption optionL = new NewOption("L", new FloatValue(0.3f), new RangeRestriction(
				new FloatValue(0.001f),
				new FloatValue(1.0f))
		);
		optionL.setDescription("Learning rate");

		
		/**
		# Momentum Rate for the back-propagation algorithm., Default = 0.2
		$ M float 1 1 r 0 0.9
		**/
		NewOption optionM = new NewOption("M", new FloatValue(0.2f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(0.9f))
		); 
		optionM.setDescription("Momentum Rate for the backpropagation algorithm");

		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 10000
		**/
		NewOption optionN = new NewOption("N", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10000))
		);
		optionN.setDescription("Number of epochs to train through");

		
		/**
		#  Percentage size of validation set to use to terminate
		#  training (if this is non zero it can pre-empt num of epochs.
		#  (Value should be between 0 - 100, Default = 0).
		$ V int 1 1 r 0 100
		**/
		NewOption optionV = new NewOption("V", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(100))
		);
		optionV.setDescription("Percentage size of validation");

		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		NewOption optionS = new NewOption("S", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionS.setDescription("Seed the random number generator");
		
		
		/**
		#  The consequetive number of errors allowed for validation
		#  testing before the netwrok terminates.
		#  (Value should be > 0, Default = 20).
		$ E int 1 1 r 0 50
		**/
		NewOption optionE = new NewOption("E", new IntegerValue(20), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(50))
		);
		optionE.setDescription("The consequetive number of errors allowed for validation");

		
		/**
		# Learning rate decay will occur; 0 arguments
		$ D boolean
		**/
		NewOption optionD = new NewOption("D", new BooleanValue(false));
		optionD.setDescription("Learning rate");

		
		/**
		# Normalizing the attributes will NOT be done.
		$ I boolean
		**/
		NewOption optionI = new NewOption("I", new BooleanValue(false));
		optionI.setDescription("Normalizing the attributes will NOT be done");
		
		
		/**
		#  GUI will be opened.
		#  (Use this to bring up a GUI).
		$ G boolean
		**/
		NewOption optionG = new NewOption("G", new BooleanValue(false));
		optionG.setDescription("GUI will be opened");
		
		
		/**
		# The hidden layers to be created for the network.
		# (Value should be a list of comma seperated Natural numbers or the letters 'a' = (attribs + classes) / 2,
		# 'i' = attribs, 'o' = classes, 't' = attribs .+ classes) For wildcard values,Default = a).
		# type list - length (2 numbers), range (2 numbers)  ... max
		$ H mixed 1 3 s 2, 3, 4, 5, 6, 7, 8, 9, 10, i, o
		**/		
		ValueType typeHn = new ValueType(new IntegerValue(2), new RangeRestriction(
				new IntegerValue(2),
				new IntegerValue(10))
		);
		ValueType typeHs = new ValueType(new StringValue("a"), new SetRestriction(false, new ArrayList<IValueData>(
				new ArrayList<IValueData>(Arrays.asList(
						new StringValue("a"), new StringValue("i"),
						new StringValue("o"), new StringValue("t")))))
		);
		TypeRestriction restriction = new TypeRestriction(Arrays.asList(typeHn, typeHs));
		NewOption optionH = new NewOption("E", new Value(
				new IntegerValue(2),
				restriction.getTypes().get(0).getRangeRestriction()),
		restriction);
		optionH.setDescription("The hidden layers to be created for the network");
		
		
		/**
		#  A NominalToBinary filter will NOT automatically be used.
		#  (Set this to not use a NominalToBinary filter).
		$ B boolean
        ***/		
		NewOption optionB = new NewOption("B", new BooleanValue(false));
		optionB.setDescription("A NominalToBinary filter will NOT automatically be used");
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		NewOption optionC = new NewOption("C", new BooleanValue(false)); 
		optionC.setDescription("Normalizing a numeric class will NOT be done");


		// TODO: not used
		Slot outputSlot = new Slot();
		outputSlot.setSlotType(SlotTypes.DATA_TYPE);
		outputSlot.setDataType("input");


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(ComputingAgent.class);
		agentInfo.importOntologyClass(Agent_WekaPerceptronCA.class);
	
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
		agentInfo.addOptions(OptionsHelper.getCAOptions());

		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getSlots_CAInput());
		agentInfo.setOutputSlots(SlotsHelper.getSlots_CAOutput());

		return agentInfo;
	}
}
