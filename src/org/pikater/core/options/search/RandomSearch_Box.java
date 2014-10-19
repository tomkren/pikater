package org.pikater.core.options.search;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.Search;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.options.SlotsHelper;

public class RandomSearch_Box {

	public static AgentInfo get() {

		NewOption optionE = new NewOption("E", new FloatValue(0.01f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
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
	
		agentInfo.setName("Random");
		agentInfo.setDescription("Selects and provides random values for its output parameters.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);

		//Slot Definition
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_Search());		

		return agentInfo;
	}

}