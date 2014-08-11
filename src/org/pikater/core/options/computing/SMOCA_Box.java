package org.pikater.core.options.computing;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class SMOCA_Box {
	
	public static AgentInfo get() {

		/**
		# -C num The complexity constant C. (default 1)
		$ C float 1 1 r 0.0001 5
		**/	
		NewOption optionC = new NewOption("C", new FloatValue(1.0f), new RangeRestriction(
				new FloatValue(0.0001f),
				new FloatValue(5.0f))
		);
		optionC.setDescription("Num The complexity");
		
		
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		NewOption optionE = new NewOption("E", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(1000))
		);
		optionE.setDescription("The exponent for the polynomial kernel");
		
		
		/**
		# -G num Gamma for the RBF kernel. (default 0.01)
		$ G float 1 1 r 0.0001 4
		**/
		NewOption optionG = new NewOption("G", new FloatValue(0.01f), new RangeRestriction(
				new FloatValue(0.0001f),
				new FloatValue(4.0f))
		);
		optionG.setDescription("Num Gamma for the RBF kernel");

		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		NewOption optionN = new NewOption("N", new IntegerValue(1), new SetRestriction(false, new ArrayList<IValueData>(Arrays.asList(
				new IntegerValue(0),
				new IntegerValue(1),
				new IntegerValue(2))))
		);
		optionN.setDescription("Num Gamma for the RBF kernel");
		
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		NewOption optionF = new NewOption("F", new BooleanValue(false));
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		
		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		NewOption optionO = new NewOption("O", new BooleanValue(false));
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		
		
		/**
		# Use the RBF kernel. (default poly)
		$ R boolean
		**/
		NewOption optionR = new NewOption("R", new BooleanValue(false));
		optionR.setDescription("Use the RBF kernel");
		
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/		
		NewOption optionA = new NewOption("A", new IntegerValue(250007), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(Integer.MAX_VALUE))
		); 
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		
		
		/**
		# -L num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		NewOption optionL = new NewOption("L", new FloatValue(1.0e-3f));
		optionL.setDescription("Sets the tolerance parameter");
		
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		NewOption optionP = new NewOption("P", new FloatValue(1.0e-12f));
		optionP.setDescription("Sets the epsilon for round-off error");
		
		
		/**
		# Fit logistic models to SVM outputs.
		$ M boolean
		**/
		NewOption optionM = new NewOption("M", new BooleanValue(false));
		optionM.setDescription("Fit logistic models to SVM outputs");
		
		
		/**
		# -V num
		# Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)
		**/
		NewOption optionV = new NewOption("V", new IntegerValue(-1),new RangeRestriction(
				new IntegerValue(-1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionV.setDescription("Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)");
		
		
		/**
		# Random number seed for cross-validation. (default 1)
		$ W int 1 1 r 1 MAXINT
		**/
		NewOption optionW = new NewOption("W", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionW.setDescription("Random number seed for cross-validation");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaSMOCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("SMO");
		agentInfo.setDescription("SMO Method");

		agentInfo.addOption(optionC);
		agentInfo.addOption(optionE);
		agentInfo.addOption(optionG);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionF);
		agentInfo.addOption(optionO);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionL);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionV);
		agentInfo.addOption(optionW);
		agentInfo.addOptions(OptionsHelper.getCAOptions());
		agentInfo.addOptions(OptionsHelper.getCAorRecommenderOptions());

		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getSlots_CAInput());
		agentInfo.setOutputSlots(SlotsHelper.getSlots_CAOutput());

		return agentInfo;
	}

}
