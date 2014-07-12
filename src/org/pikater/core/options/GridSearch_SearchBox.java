package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;


public class GridSearch_SearchBox {
	
	public static AgentInfo get() {
		
		ValueType typeB = new ValueType(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		TypeRestrictions restrictionB = new TypeRestrictions();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"B" );
		optionB.setDescription("Maximum block size");
		optionB.setTypeRestrictions(restrictionB);

		
		ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		TypeRestrictions restrictionN = new TypeRestrictions();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Default number of tries");
		optionN.setTypeRestrictions(restrictionN);
		
		
		ValueType typeZ = new ValueType(FloatValue.class);
		typeZ.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1000.0f) ));
		TypeRestrictions restrictionZ = new TypeRestrictions();
		restrictionZ.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeZ )) ));
		
		NewOption optionZ = new NewOption(
				new FloatValue(10),
				new ValueType(FloatValue.class),
				"Z" );
		optionZ.setDescription("Zero for logarithmic steps");
		optionZ.setTypeRestrictions(restrictionZ);
				
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_GridSearch.class);
		agentInfo.setOntologyClass(Search.class);
	
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
