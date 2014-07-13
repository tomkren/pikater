package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class RBFNetwork_CABox {
	
	public static AgentInfo get() {

		/**
		# number of clusters, default 2
		$ B int 1 1 r 2 1000
		**/
		ValueType typeB = new ValueType(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(2), new IntegerValue(1000) ));
		RestrictionsForOption restrictionB = new RestrictionsForOption();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(2),
				new ValueType(IntegerValue.class),
				"B" );
		optionB.setDescription("Number of clusters");
		optionB.setTypeRestrictions(restrictionB);
		
		
		/**
		# minStdDev, default 0.1
		$ W float 1 1 r 0.01 2
		**/
		ValueType typeW = new ValueType(FloatValue.class);
		typeW.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.01f), new FloatValue(2.0f) ));
		RestrictionsForOption restrictionW = new RestrictionsForOption();
		restrictionW.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeW )) ));
		
		NewOption optionW = new NewOption(
				new FloatValue(0.25f),
				new ValueType(FloatValue.class),
				"W" );
		optionW.setDescription("minStdDev");
		optionW.setTypeRestrictions(restrictionW);
		
		
		/**
		# Ridge (Set the Ridge value for the logistic or linear regression), default 1.0E-8
		$ R float 1 1 r 0.000000001 10
		**/
		ValueType typeR = new ValueType(FloatValue.class);
		typeR.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.000000001f), new FloatValue(10.0f) ));
		RestrictionsForOption restrictionR = new RestrictionsForOption();
		restrictionR.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new FloatValue(1.0e-8f),
				new ValueType(FloatValue.class),
				"R" );
		optionR.setDescription("Ridge (Set the Ridge value for the logistic or linear regression)");
		optionR.setTypeRestrictions(restrictionR);
		
		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		ValueType typeS = new ValueType(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		RestrictionsForOption restrictionS = new RestrictionsForOption();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"S" );
		optionS.setDescription("The value used to seed the random number generator");
		optionS.setTypeRestrictions(restrictionS);
		
		
		/**
		#  Set the maximum number of iterations for the logistic regression. (default -1, until convergence).
		$ M int 1 1 r -1 50
		**/
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(-1), new IntegerValue(50) ));
		RestrictionsForOption restrictionM = new RestrictionsForOption();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(-1),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("Set the maximum number of iterations for the logistic regression");
		optionM.setTypeRestrictions(restrictionM);



		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaRBFNetworkCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);

		agentInfo.setName("RBFNetwork");
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
