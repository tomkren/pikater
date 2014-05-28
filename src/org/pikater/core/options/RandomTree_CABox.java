package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaRandomTreeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class RandomTree_CABox {
	
	public static AgentInfo get() {

		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		Type typeK = new Type(IntegerValue.class);
		typeK.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(50) ));
		PossibleTypesRestriction restrictionK = new PossibleTypesRestriction();
		restrictionK.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeK )) );
		
		NewOption optionK = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"K" );
		optionK.setDescription("Sets the number of randomly chosen attributes");
		optionK.setPossibleTypesRestriction(restrictionK);
		
		
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeM )) );
		
		NewOption optionM = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("The minimum total weight of the instances in a leaf");
		optionM.setPossibleTypesRestriction(restrictionM);
		
		
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		Type typeQ = new Type(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionQ = new PossibleTypesRestriction();
		restrictionQ.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeQ )) );
		
		NewOption optionQ = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The random number seed used for selecting attributes");
		optionQ.setPossibleTypesRestriction(restrictionQ);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaRandomTreeCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("RandomTree");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Random Tree Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionQ);

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
