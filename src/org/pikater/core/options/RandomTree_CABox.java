package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaRandomTreeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class RandomTree_CABox {
	
	public static AgentInfo get() {

		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		ValueType typeK = new ValueType(IntegerValue.class);
		typeK.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(50) ));
		TypeRestrictions restrictionK = new TypeRestrictions();
		restrictionK.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeK )) ));
		
		NewOption optionK = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"K" );
		optionK.setDescription("Sets the number of randomly chosen attributes");
		optionK.setTypeRestrictions(restrictionK);
		
		
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestrictions restrictionM = new TypeRestrictions();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("The minimum total weight of the instances in a leaf");
		optionM.setTypeRestrictions(restrictionM);
		
		
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		ValueType typeQ = new ValueType(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		TypeRestrictions restrictionQ = new TypeRestrictions();
		restrictionQ.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeQ )) ));
		
		NewOption optionQ = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The random number seed used for selecting attributes");
		optionQ.setTypeRestrictions(restrictionQ);


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaRandomTreeCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("RandomTree");
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
