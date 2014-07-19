package org.pikater.core.options;

import org.pikater.core.agents.experiment.search.Agent_EASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;


public class EASearch_SearchBox {

	public static AgentInfo get() {

		NewOption optionE = new NewOption("E", new FloatValue(0.1f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionE.setDescription("Minimum error rate");
		

		// TODO: chyba
		NewOption optionM = new NewOption("M", new FloatValue(0.1f), new RangeRestriction(
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
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		); 
		optionX.setDescription("Crossover probability");
		
		
		NewOption optionP = new NewOption("P", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		); 
		optionP.setDescription("Population size");
		

		NewOption optionI = new NewOption("I", new IntegerValue(10), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		); 
		optionI.setDescription("Maximum number of option evaluations");


		NewOption optionF = new NewOption("F", new FloatValue(0.2f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionF.setDescription("Mutation rate per field in individual");


		NewOption optionL = new NewOption("L", new FloatValue(0.1f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		); 
		optionL.setDescription("The percentage of elite individuals");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_EASearch.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("EASearch");
		agentInfo.setDescription("Searcher using Evolution algorithm");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionX);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionI);
		agentInfo.addOption(optionF);
		agentInfo.addOption(optionL);

		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
		
		return agentInfo;
	}
}
