package org.pikater.core.options.search;

import org.pikater.core.agents.experiment.search.Agent_GASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.AgentDefinitionHelper;

public class GASearch_Box {
	
	public static AgentInfo get() {

		NewOption optionE = new NewOption("E", new FloatValue(0.1f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionE.setDescription("Minimum error rate");


		NewOption optionM = new NewOption("M", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(1000))
		); 
		optionM.setDescription("Maximal number of generations");

		
		NewOption optionT = new NewOption("T", new FloatValue(0.2f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionT.setDescription("Mutation rate");

		
		NewOption optionX = new NewOption("X", new FloatValue(0.5f), new RangeRestriction(
				new FloatValue(0.0f), new FloatValue(1.0f))
		); 
		optionX.setDescription("Crossover probability");

		
		NewOption optionP = new NewOption("P", new IntegerValue(5), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		); 
		optionP.setDescription("Population size");


		NewOption optionS = new NewOption("S", new IntegerValue(2), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		); 
		optionS.setDescription("Size of tournament in selection");

		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_GASearch.class);
		agentInfo.importOntologyClass(Search.class);
	
		agentInfo.setName("GASearch");
		agentInfo.setDescription("Searcher using Genetic algorithm");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionX);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionS);


		//Slot Definition
		agentInfo.setOutputSlots(AgentDefinitionHelper.getSearcherOutputSlots());

		return agentInfo;
	}

}
