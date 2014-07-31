package org.pikater.core.options.search;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.SlotsHelper;


public class GridSearch_Box {
	
	public static AgentInfo get() {
		
		NewOption optionB = new NewOption("B", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100000))
		);
		optionB.setDescription("Maximum block size");

		
		NewOption optionN = new NewOption("N", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100000))
		);
		optionN.setDescription("Default number of tries");
		
		
		NewOption optionZ = new NewOption("Z", new FloatValue(10), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1000.0f))
		);
		optionZ.setDescription("Zero for logarithmic steps");
		
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_GridSearch.class);
		agentInfo.importOntologyClass(Search.class);
	
		agentInfo.setName("Grid");
		agentInfo.setDescription("GridSearch Description");
		
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionZ);

		//Slot Definition
		agentInfo.setOutputSlots(SlotsHelper.getSlots_SearchOutput());
		
		return agentInfo;
	}

}
