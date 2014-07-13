package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class RandomSearch_SearchBox {

	public static AgentInfo get() {

		ValueType typeE = new ValueType(DoubleValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		RestrictionsForOption restrictionE = new RestrictionsForOption();
		restrictionE.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new DoubleValue(0.01),
				new ValueType(DoubleValue.class),
				"E" );
		optionE.setDescription("E");
		optionE.setTypeRestrictions(restrictionE);
		
		
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100000) ));
		RestrictionsForOption restrictionM = new RestrictionsForOption();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("M");
		optionM.setTypeRestrictions(restrictionM);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_RandomSearch.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("Random-Searcher");
		agentInfo.setDescription("Selects and provides random values for its output parameters.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}