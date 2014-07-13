package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOregCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class SMOReg_CABox  {
	
	public static AgentInfo get() {

		/**
		# -S num
		# The amount up to which deviation are tolerated (epsilon). (default 1e-3)
		# Watch out, the value of epsilon is used with the (normalized/standardize) data
		**/
		ValueType typeS = new ValueType(FloatValue.class);
		RestrictionsForOption restrictionS = new RestrictionsForOption();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new FloatValue(1e-3f),
				new ValueType(FloatValue.class),
				"S" );
		optionS.setDescription("The amount up to which deviation are tolerated (epsilon)");
		optionS.setTypeRestrictions(restrictionS);
		
		/**
		# -C num
		# The complexity constant C. (default 1)
		**/
		ValueType typeC = new ValueType(IntegerValue.class);
		RestrictionsForOption restrictionC = new RestrictionsForOption();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"C" );
		optionC.setDescription("The complexity constant");
		optionC.setTypeRestrictions(restrictionC);
		

		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		ValueType typeE = new ValueType(IntegerValue.class);
		RestrictionsForOption restrictionE = new RestrictionsForOption();
		restrictionE.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"E" );
		optionE.setDescription("The exponent for the polynomial kernel");
		optionE.setTypeRestrictions(restrictionE);
		
		
		/**
		# -G num
		# Gamma for the RBF kernel. (default 0.01)
		**/
		ValueType typeG = new ValueType(FloatValue.class);
		RestrictionsForOption restrictionG = new RestrictionsForOption();
		restrictionG.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new FloatValue(0.01f),
				new ValueType(FloatValue.class),
				"G" );
		optionG.setDescription("Gamma for the RBF kernel");
		optionG.setTypeRestrictions(restrictionG);
		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setSetRestriction(
				new SetRestriction(
						new ArrayList<ITypedValue>(
								Arrays.asList( new IntegerValue(0), new IntegerValue(1), new IntegerValue(2) )) ));
		RestrictionsForOption restrictionN = new RestrictionsForOption();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Random number seed for cross-validation");
		optionN.setTypeRestrictions(restrictionN);
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		ValueType typeF = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionF = new RestrictionsForOption();
		restrictionF.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeF )) ));
		
		NewOption optionF = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"F" );
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		optionF.setTypeRestrictions(restrictionF);
		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		ValueType typeO = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionO = new RestrictionsForOption();
		restrictionO.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeO )) ));
		
		NewOption optionO = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"O" );
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		optionO.setTypeRestrictions(restrictionO);
		
		/**
		# Use RBF kernel (default poly).
		$ R boolean
		**/
		ValueType typeR = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionR = new RestrictionsForOption();
		restrictionR.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"R" );
		optionR.setDescription("Use RBF kernel (default poly)");
		optionR.setTypeRestrictions(restrictionR);
		
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/
		ValueType typeA = new ValueType(IntegerValue.class);
		typeA.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		RestrictionsForOption restrictionA = new RestrictionsForOption();
		restrictionA.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeA )) ));
		
		NewOption optionA = new NewOption(
				new IntegerValue(250007),
				new ValueType(IntegerValue.class),
				"A" );
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		optionA.setTypeRestrictions(restrictionA);
		
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		ValueType typeP = new ValueType(FloatValue.class);
		RestrictionsForOption restrictionP = new RestrictionsForOption();
		restrictionP.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new FloatValue(1.0e-12f),
				new ValueType(FloatValue.class),
				"P" );
		optionP.setDescription("Sets the epsilon for round-off error");
		optionP.setTypeRestrictions(restrictionP);
		
		
		/**
		# -T num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		ValueType typeT = new ValueType(FloatValue.class);
		RestrictionsForOption restrictionT = new RestrictionsForOption();
		restrictionT.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new FloatValue(1.0e-3f),
				new ValueType(FloatValue.class),
				"T" );
		optionT.setDescription("Sets the epsilon for round-off error");
		optionT.setTypeRestrictions(restrictionT);

		

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
