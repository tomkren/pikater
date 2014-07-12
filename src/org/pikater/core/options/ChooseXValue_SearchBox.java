package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class ChooseXValue_SearchBox {
	
	public static AgentInfo get() {

		final ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(2000) ));
		TypeRestrictions restrictionN = new TypeRestrictions();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(5),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of values to try for each option");
		optionN.setTypeRestrictions(restrictionN);
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_ChooseXValues.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("Choose X Values Agent");
		agentInfo.setDescription("Search which Choose X Values");

		agentInfo.addOption(optionN);
		
		// Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());

		return agentInfo;
	}

}
