package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.value.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class RandomSearch_SearchBox {

	public static AgentInfo get() {

		Type typeE = new Type(DoubleValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		PossibleTypesRestriction restrictionE = new PossibleTypesRestriction();
		restrictionE.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeE )) );
		
		NewOption optionE = new NewOption(
				new DoubleValue(0.01),
				new Type(DoubleValue.class),
				"E" );
		optionE.setDescription("E");
		optionE.setPossibleTypesRestriction(restrictionE);
		
		
		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues(
				new ArrayList<Type>(Arrays.asList( typeM )) );
		
		NewOption optionM = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("M");
		optionM.setPossibleTypesRestriction(restrictionM);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_RandomSearch.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("Random-Searcher");
		agentInfo.setPicture("picture1.jpg");
		agentInfo.setDescription("Selects and provides random values for its output parameters.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}