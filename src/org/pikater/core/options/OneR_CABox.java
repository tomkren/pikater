package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaOneRCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class OneR_CABox {
	

	public static AgentInfo get() {

		/**
		# Specify the minimum number of objects in a bucket (default: 6).
		$ B int 1 1 r 1 100
		**/
		Type typeB = new Type(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(6),
				new Type(IntegerValue.class),
				"B" );
		optionB.setDescription("Specify the minimum number of objects in a bucket");
		optionB.setPossibleTypesRestriction(restrictionB);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaOneRCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("OneR");
		agentInfo.setDescription("One R Method");

		agentInfo.addOption(optionB);
		

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
