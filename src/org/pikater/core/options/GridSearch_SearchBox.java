package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;


public class GridSearch_SearchBox {
	
	public static AgentInfo get() {
		
		Type typeB = new Type(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"B" );
		optionB.setDescription("Maximum block size");
		optionB.setPossibleTypesRestriction(restrictionB);

		
		Type typeN = new Type(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Default number of tries");
		optionN.setPossibleTypesRestriction(restrictionN);
		
		
		Type typeZ = new Type(FloatValue.class);
		typeZ.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1000.0f) ));
		PossibleTypesRestriction restrictionZ = new PossibleTypesRestriction();
		restrictionZ.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeZ )) ));
		
		NewOption optionZ = new NewOption(
				new FloatValue(10),
				new Type(FloatValue.class),
				"Z" );
		optionZ.setDescription("Zero for logarithmic steps");
		optionZ.setPossibleTypesRestriction(restrictionZ);
				
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Search.class);
		agentInfo.setOntologyClass(Agent_GridSearch.class);
	
		agentInfo.setName("GridSearch");
		agentInfo.setDescription("GridSearch Description");
		
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionZ);

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
		
		return agentInfo;
	}

}
