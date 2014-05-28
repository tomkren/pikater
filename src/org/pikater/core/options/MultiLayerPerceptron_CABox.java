package org.pikater.core.options;

import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;


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
		OptionDefault optionL = new OptionDefault();
		optionL.setName("L");
		optionL.setDescription("Learning rate");
		optionL.setValue(
				new OptionValue(new Float(0.3f)) );
		optionL.setInterval(
				new OptionInterval(new Float(0.001f), new Float(1.0f)) );
		optionL.setList(
				new OptionList() );
		
		
		/**
		# Momentum Rate for the backpropagation algorithm., Default = 0.2
		$ M float 1 1 r 0 0.9
		**/
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Momentum Rate for the backpropagation algorithm");
		optionM.setValue(
				new OptionValue(new Float(0.2f)) );
		optionL.setInterval(
				new OptionInterval(new Float(0.0f), new Float(0.9f)) );
		optionL.setList(
				new OptionList() );
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 10000
		**/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Number of epochs to train through");
		optionN.setValue(
				new OptionValue(new Integer(1)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(10000)) );
		optionN.setList(
				new OptionList() );
		
		/**
		#  Percentage size of validation set to use to terminate
		#  training (if this is non zero it can pre-empt num of epochs.
		#  (Value should be between 0 - 100, Default = 0).
		$ V int 1 1 r 0 100
		**/
		OptionDefault optionV = new OptionDefault();
		optionV.setName("V");
		optionV.setDescription("Percentage size of validation");
		optionV.setValue(
				new OptionValue(new Integer(0)) );
		optionV.setInterval(
				new OptionInterval(new Integer(0), new Integer(100)) );
		optionV.setList(
				new OptionList() );
		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("Seed the random number generator");
		optionS.setValue(
				new OptionValue(new Integer(0)) );
		optionS.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		optionS.setList(
				new OptionList() );
		
		/**
		#  The consequetive number of errors allowed for validation
		#  testing before the netwrok terminates.
		#  (Value should be > 0, Default = 20).
		$ E int 1 1 r 0 50
		**/
		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("The consequetive number of errors allowed for validation");
		optionE.setValue(
				new OptionValue(new Integer(20)) );
		optionE.setInterval(
				new OptionInterval(new Integer(0), new Integer(50)) );
		optionE.setList(
				new OptionList() );
		
		/**
		# Learning rate decay will occur; 0 arguments
		$ D boolean
		**/
		OptionDefault optionD = new OptionDefault();
		optionD.setName("D");
		optionD.setDescription("Learning rate");
		optionD.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# Normalizing the attributes will NOT be done.
		$ I boolean
		**/
		OptionDefault optionI = new OptionDefault();
		optionI.setName("I");
		optionI.setDescription("Normalizing the attributes will NOT be done");
		optionI.setValue(
				new OptionValue(new Boolean(false)) );

		
		/**
		#  GUI will be opened.
		#  (Use this to bring up a GUI).
		$ G boolean
		**/
		OptionDefault optionG = new OptionDefault();
		optionG.setName("G");
		optionG.setDescription("GUI will be opened");
		optionG.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# The hidden layers to be created for the network.
		# (Value should be a list of comma seperated Natural numbers or the letters 'a' = (attribs + classes) / 2,
		# 'i' = attribs, 'o' = classes, 't' = attribs .+ classes) For wildcard values,Default = a).
		# type list - length (2 numbers), range (2 numbers)  ... max
		$ H mixed 1 3 s 2, 3, 4, 5, 6, 7, 8, 9, 10, i, o
		**/		
		
		OptionDefault optionH = new OptionDefault();
		optionH.setName("H");
		optionH.setDescription("The hidden layers to be created for the network");
		optionH.setValue( null );
		optionH.setInterval( null );
		OptionList listH = new OptionList();
		listH.setList(Arrays.asList(new Object[] {2, 3, 4, 5, 6, 7, 8, 9, 10, "i", "o"}));
		optionH.setList(
				listH );
		
		/**
		#  A NominalToBinary filter will NOT automatically be used.
		#  (Set this to not use a NominalToBinary filter).
		$ B boolean
        ***/
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("A NominalToBinary filter will NOT automatically be used");
		optionB.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/		
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setValue(
				new OptionValue(new Boolean(false)) );


		Slot outputSlot = new Slot();
		outputSlot.setSlotType("input");
		outputSlot.setDataType(SlotTypes.DATA_TYPE);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(ComputingAgent.class);
		agentInfo.setOntologyClass(Agent_WekaPerceptronCA.class);
	
		agentInfo.setName("MultiLayerPerceptron");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Multi-layer perceptron method");

		agentInfo.addOption(optionC.toOption());
		agentInfo.addOption(optionB.toOption());
		agentInfo.addOption(optionG.toOption());
		agentInfo.addOption(optionI.toOption());
		agentInfo.addOption(optionD.toOption());
		agentInfo.addOption(optionE.toOption());
		agentInfo.addOption(optionS.toOption());
		agentInfo.addOption(optionV.toOption());
		agentInfo.addOption(optionN.toOption());
		agentInfo.addOption(optionM.toOption());
		agentInfo.addOption(optionL.toOption());
		agentInfo.addOption(optionH.toOption());


		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}
}
