package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class RBFNetwork_CABox {
	
	public static AgentInfo get() {

		/**
		# number of clusters, default 2
		$ B int 1 1 r 2 1000
		**/
		Type typeB = new Type(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(2), new IntegerValue(1000) ));
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(2),
				new Type(IntegerValue.class),
				"B" );
		optionB.setDescription("Number of clusters");
		optionB.setPossibleTypesRestriction(restrictionB);
		
		
		/**
		# minStdDev, default 0.1
		$ W float 1 1 r 0.01 2
		**/
		Type typeW = new Type(FloatValue.class);
		typeW.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.01f), new FloatValue(2.0f) ));
		PossibleTypesRestriction restrictionW = new PossibleTypesRestriction();
		restrictionW.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeW )) ));
		
		NewOption optionW = new NewOption(
				new FloatValue(0.25f),
				new Type(FloatValue.class),
				"W" );
		optionW.setDescription("minStdDev");
		optionW.setPossibleTypesRestriction(restrictionW);
		
		
		/**
		# Ridge (Set the Ridge value for the logistic or linear regression), default 1.0E-8
		$ R float 1 1 r 0.000000001 10
		**/
		Type typeR = new Type(FloatValue.class);
		typeR.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.000000001f), new FloatValue(10.0f) ));
		PossibleTypesRestriction restrictionR = new PossibleTypesRestriction();
		restrictionR.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new FloatValue(1.0e-8f),
				new Type(FloatValue.class),
				"R" );
		optionR.setDescription("Ridge (Set the Ridge value for the logistic or linear regression)");
		optionR.setPossibleTypesRestriction(restrictionR);
		
		
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
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"S" );
		optionS.setDescription("The value used to seed the random number generator");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		
		/**
		#  Set the maximum number of iterations for the logistic regression. (default -1, until convergence).
		$ M int 1 1 r -1 50
		**/
		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(-1), new IntegerValue(50) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(-1),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("Set the maximum number of iterations for the logistic regression");
		optionM.setPossibleTypesRestriction(restrictionM);



		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaRBFNetworkCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);

		agentInfo.setName("RBFNetwork");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("RBFNetwork Method");

		agentInfo.addOption(optionB);
		agentInfo.addOption(optionW);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionM);

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
