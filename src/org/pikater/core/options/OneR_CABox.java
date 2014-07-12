package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaOneRCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;

public class OneR_CABox {
	

	public static AgentInfo get() {

		/**
		# Specify the minimum number of objects in a bucket (default: 6).
		$ B int 1 1 r 1 100
		**/
		ValueType typeB = new ValueType(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestrictions restrictionB = new TypeRestrictions();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(6),
				new ValueType(IntegerValue.class),
				"B" );
		optionB.setDescription("Specify the minimum number of objects in a bucket");
		optionB.setTypeRestrictions(restrictionB);
		

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
