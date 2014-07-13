package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;


public class GridSearch_SearchBox {
	
	public static AgentInfo get() {
		
		ValueType typeB = new ValueType(IntegerValue.class);
		typeB.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		RestrictionsForOption restrictionB = new RestrictionsForOption();
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
		RestrictionsForOption restrictionN = new RestrictionsForOption();
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
		RestrictionsForOption restrictionZ = new RestrictionsForOption();
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
