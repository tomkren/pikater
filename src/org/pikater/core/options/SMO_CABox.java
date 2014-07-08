package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;

public class SMO_CABox {
	
	public static AgentInfo get() {

		/**
		# -C num The complexity constant C. (default 1)
		$ C float 1 1 r 0.0001 5
		**/	
		Type typeC = new Type(FloatValue.class);
		typeC.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0001f), new FloatValue(5.0f) ));
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(1.0f),
				new Type(FloatValue.class),
				"C" );
		optionC.setDescription("Num The complexity");
		optionC.setPossibleTypesRestriction(restrictionC);
		
		
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		Type typeE = new Type(IntegerValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		PossibleTypesRestriction restrictionE = new PossibleTypesRestriction();
		restrictionE.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"E" );
		optionE.setDescription("The exponent for the polynomial kernel");
		optionE.setPossibleTypesRestriction(restrictionE);
		
		
		/**
		# -G num Gamma for the RBF kernel. (default 0.01)
		$ G float 1 1 r 0.0001 4
		**/
		Type typeG = new Type(FloatValue.class);
		typeG.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0001f), new FloatValue(4.0f) ));
		PossibleTypesRestriction restrictionG = new PossibleTypesRestriction();
		restrictionG.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new FloatValue(0.01f),
				new Type(FloatValue.class),
				"G" );
		optionG.setDescription("Num Gamma for the RBF kernel");
		optionG.setPossibleTypesRestriction(restrictionG);

		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		Type typeN = new Type(IntegerValue.class);
		typeN.setSetRestriction(
				new SetRestriction(
						new ArrayList<ITypedValue>(
								Arrays.asList( new IntegerValue(0), new IntegerValue(1), new IntegerValue(2) ) )));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Num Gamma for the RBF kernel");
		optionN.setPossibleTypesRestriction(restrictionN);
		
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		Type typeF = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionF = new PossibleTypesRestriction();
		restrictionF.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeF )) ));
		
		NewOption optionF = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"F" );
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		optionF.setPossibleTypesRestriction(restrictionF);
		
		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		Type typeO = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionO = new PossibleTypesRestriction();
		restrictionO.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeO )) ));
		
		NewOption optionO = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"O" );
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		optionO.setPossibleTypesRestriction(restrictionO);
		
		
		/**
		# Use the RBF kernel. (default poly)
		$ R boolean
		**/
		Type typeR = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionR = new PossibleTypesRestriction();
		restrictionR.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"R" );
		optionR.setDescription("Use the RBF kernel");
		optionR.setPossibleTypesRestriction(restrictionR);
		
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/		
		Type typeA = new Type(IntegerValue.class);
		typeA.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionA = new PossibleTypesRestriction();
		restrictionA.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeA )) ));
		
		NewOption optionA = new NewOption(
				new IntegerValue(250007),
				new Type(IntegerValue.class),
				"A" );
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		optionA.setPossibleTypesRestriction(restrictionA);
		
		
		/**
		# -L num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		Type typeL = new Type(FloatValue.class);
		PossibleTypesRestriction restrictionL = new PossibleTypesRestriction();
		restrictionL.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(1.0e-3f),
				new Type(FloatValue.class),
				"L" );
		optionL.setDescription("Sets the tolerance parameter");
		optionL.setPossibleTypesRestriction(restrictionL);
		
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		Type typeP = new Type(FloatValue.class);
		PossibleTypesRestriction restrictionP = new PossibleTypesRestriction();
		restrictionP.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new FloatValue(1.0e-12f),
				new Type(FloatValue.class),
				"P" );
		optionP.setDescription("Sets the epsilon for round-off error");
		optionP.setPossibleTypesRestriction(restrictionP);
		
		
		/**
		# Fit logistic models to SVM outputs.
		$ M boolean
		**/
		Type typeM = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"M" );
		optionM.setDescription("Fit logistic models to SVM outputs");
		optionM.setPossibleTypesRestriction(restrictionM);
		
		
		/**
		# -V num
		# Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)
		**/
		Type typeV = new Type(IntegerValue.class);
		PossibleTypesRestriction restrictionV = new PossibleTypesRestriction();
		restrictionV.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeV )) ));
		
		NewOption optionV = new NewOption(
				new IntegerValue(-1),
				new Type(IntegerValue.class),
				"V" );
		optionV.setDescription("Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)");
		optionV.setPossibleTypesRestriction(restrictionV);
		
		
		/**
		# Random number seed for cross-validation. (default 1)
		$ W int 1 1 r 1 MAXINT
		**/
		Type typeW = new Type(IntegerValue.class);
		typeW.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionW = new PossibleTypesRestriction();
		restrictionW.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeW )) ));
		
		NewOption optionW = new NewOption(
				new IntegerValue(-1),
				new Type(IntegerValue.class),
				"W" );
		optionW.setDescription("Random number seed for cross-validation");
		optionW.setPossibleTypesRestriction(restrictionW);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaSMOCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
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


		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
