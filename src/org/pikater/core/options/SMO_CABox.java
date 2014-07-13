package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOCA;
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

public class SMO_CABox {
	
	public static AgentInfo get() {

		/**
		# -C num The complexity constant C. (default 1)
		$ C float 1 1 r 0.0001 5
		**/	
		ValueType typeC = new ValueType(FloatValue.class);
		typeC.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0001f), new FloatValue(5.0f) ));
		RestrictionsForOption restrictionC = new RestrictionsForOption();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(1.0f),
				new ValueType(FloatValue.class),
				"C" );
		optionC.setDescription("Num The complexity");
		optionC.setTypeRestrictions(restrictionC);
		
		
		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		ValueType typeE = new ValueType(IntegerValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
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
		# -G num Gamma for the RBF kernel. (default 0.01)
		$ G float 1 1 r 0.0001 4
		**/
		ValueType typeG = new ValueType(FloatValue.class);
		typeG.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0001f), new FloatValue(4.0f) ));
		RestrictionsForOption restrictionG = new RestrictionsForOption();
		restrictionG.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new FloatValue(0.01f),
				new ValueType(FloatValue.class),
				"G" );
		optionG.setDescription("Num Gamma for the RBF kernel");
		optionG.setTypeRestrictions(restrictionG);

		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setSetRestriction(
				new SetRestriction(
						new ArrayList<ITypedValue>(
								Arrays.asList( new IntegerValue(0), new IntegerValue(1), new IntegerValue(2) ) )));
		RestrictionsForOption restrictionN = new RestrictionsForOption();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Num Gamma for the RBF kernel");
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
		# Use the RBF kernel. (default poly)
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
		optionR.setDescription("Use the RBF kernel");
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
		# -L num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		ValueType typeL = new ValueType(FloatValue.class);
		RestrictionsForOption restrictionL = new RestrictionsForOption();
		restrictionL.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(1.0e-3f),
				new ValueType(FloatValue.class),
				"L" );
		optionL.setDescription("Sets the tolerance parameter");
		optionL.setTypeRestrictions(restrictionL);
		
		
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
		# Fit logistic models to SVM outputs.
		$ M boolean
		**/
		ValueType typeM = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionM = new RestrictionsForOption();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"M" );
		optionM.setDescription("Fit logistic models to SVM outputs");
		optionM.setTypeRestrictions(restrictionM);
		
		
		/**
		# -V num
		# Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)
		**/
		ValueType typeV = new ValueType(IntegerValue.class);
		RestrictionsForOption restrictionV = new RestrictionsForOption();
		restrictionV.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeV )) ));
		
		NewOption optionV = new NewOption(
				new IntegerValue(-1),
				new ValueType(IntegerValue.class),
				"V" );
		optionV.setDescription("Number of folds for cross-validation used to generate data for logistic models. (default -1, use training data)");
		optionV.setTypeRestrictions(restrictionV);
		
		
		/**
		# Random number seed for cross-validation. (default 1)
		$ W int 1 1 r 1 MAXINT
		**/
		ValueType typeW = new ValueType(IntegerValue.class);
		typeW.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		RestrictionsForOption restrictionW = new RestrictionsForOption();
		restrictionW.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeW )) ));
		
		NewOption optionW = new NewOption(
				new IntegerValue(-1),
				new ValueType(IntegerValue.class),
				"W" );
		optionW.setDescription("Random number seed for cross-validation");
		optionW.setTypeRestrictions(restrictionW);
		

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
