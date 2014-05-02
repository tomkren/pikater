package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.Search;

public class RandomSearch_SearchBox {

	public static AgentInfo get() {

		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("E");
		optionE.setValue(
				new OptionValue( new Double(0.01)) );
		optionE.setInterval(
				new OptionInterval(new Double(0.0), new Double(1.0)) );
		optionE.setList( new OptionList() );
		
		
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("M");
		optionM.setValue(
				new OptionValue( new Integer(10)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(100000)) );
		optionM.setList( new OptionList() );
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_RandomSearch.class.getName());
		agentInfo.setOntologyClass(Search.class.getName());
	
		agentInfo.setName("Random-Searcher");
		agentInfo.setPicture("picture1.jpg");
		agentInfo.setDescription("Selects and provides random values for its output parameters.");

		agentInfo.addOption(optionE.toOption());
		agentInfo.addOption(optionM.toOption());

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}