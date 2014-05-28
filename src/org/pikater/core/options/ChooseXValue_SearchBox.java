package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class ChooseXValue_SearchBox {
	
	public static AgentInfo get() {

		final Type typeN = new Type(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(2000) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeN )) );
		
		NewOption optionN = new NewOption(
				new IntegerValue(5),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of values to try for each option");
		optionN.setPossibleTypesRestriction(restrictionN);
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_ChooseXValues.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("Choose X Values Agent");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("Search which Choose X Values");

		agentInfo.addOption(optionN);
		
		// Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());

		return agentInfo;
	}

}
