package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOregCA;
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

public class SMOReg_CABox  {
	
	public static AgentInfo get() {

		/**
		# -S num
		# The amount up to which deviation are tolerated (epsilon). (default 1e-3)
		# Watch out, the value of epsilon is used with the (normalized/standardize) data
		**/
		Type typeS = new Type(FloatValue.class);
		PossibleTypesRestriction restrictionS = new PossibleTypesRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new FloatValue(1e-3f),
				new Type(FloatValue.class),
				"S" );
		optionS.setDescription("The amount up to which deviation are tolerated (epsilon)");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		/**
		# -C num
		# The complexity constant C. (default 1)
		**/
		Type typeC = new Type(IntegerValue.class);
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"C" );
		optionC.setDescription("The complexity constant");
		optionC.setPossibleTypesRestriction(restrictionC);
		

		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		Type typeE = new Type(IntegerValue.class);
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
		# -G num
		# Gamma for the RBF kernel. (default 0.01)
		**/
		Type typeG = new Type(FloatValue.class);
		PossibleTypesRestriction restrictionG = new PossibleTypesRestriction();
		restrictionG.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new FloatValue(0.01f),
				new Type(FloatValue.class),
				"G" );
		optionG.setDescription("Gamma for the RBF kernel");
		optionG.setPossibleTypesRestriction(restrictionG);
		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		Type typeN = new Type(IntegerValue.class);
		typeN.setSetRestriction(
				new SetRestriction(
						new ArrayList<ITypedValue>(
								Arrays.asList( new IntegerValue(0), new IntegerValue(1), new IntegerValue(2) )) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Random number seed for cross-validation");
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
		# Use RBF kernel (default poly).
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
		optionR.setDescription("Use RBF kernel (default poly)");
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
		# -T num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		Type typeT = new Type(FloatValue.class);
		PossibleTypesRestriction restrictionT = new PossibleTypesRestriction();
		restrictionT.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new FloatValue(1.0e-3f),
				new Type(FloatValue.class),
				"T" );
		optionT.setDescription("Sets the epsilon for round-off error");
		optionT.setPossibleTypesRestriction(restrictionT);

		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaSMOregCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("SMO Reg");
		agentInfo.setDescription("SMOReg Method");

		agentInfo.addOption(optionS);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionE);
		agentInfo.addOption(optionG);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionF);
		agentInfo.addOption(optionO);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionT);

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
