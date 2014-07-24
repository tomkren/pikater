package org.pikater.core.options.search;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.AAA_SlotHelper;

public class RandomSearch_Box {

	public static AgentInfo get() {

		NewOption optionE = new NewOption("E", new DoubleValue(0.01), new RangeRestriction(
				new DoubleValue(0.0),
				new DoubleValue(1.0))
		);
		optionE.setDescription("E");
		
		
		NewOption optionM = new NewOption("M", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100000))
		);
		optionM.setDescription("M");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_RandomSearch.class);
		agentInfo.importOntologyClass(Search.class);
	
		agentInfo.setName("Random-Searcher");
		agentInfo.setDescription("Selects and provides random values for its output parameters.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}