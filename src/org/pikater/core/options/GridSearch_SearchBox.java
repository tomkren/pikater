package org.pikater.core.options;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.description.Search;


public class GridSearch_SearchBox {
	
	public static AgentInfo get() {
		
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("Maximum block size");
		optionB.setValue(
				new OptionValue(new Integer(10)) );
		optionB.setInterval(
				new OptionInterval(new Integer(1), new Integer(100000)) );
		optionB.setList(
				new OptionList() );
		
		
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Default number of tries");
		optionN.setValue(
				new OptionValue(new Integer(10)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(100000)) );
		optionN.setList(
				new OptionList() );
		
		
		OptionDefault optionZ = new OptionDefault();
		optionZ.setName("Z");
		optionZ.setDescription("Zero for logarithmic steps");
		optionZ.setValue(
				new OptionValue(new Float(0.000000001f)) );
		optionZ.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1000.0f)) );
		optionZ.setList(
				new OptionList() );

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Search.class.getName()); // some virtual-box provider agent
		agentInfo.setOntologyClass(Agent_GridSearch.class.getName());
	
		agentInfo.setName("GridSearch");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("GridSearch Description");
		
		agentInfo.addOption(optionB.toOption());
		agentInfo.addOption(optionN.toOption());
		agentInfo.addOption(optionZ.toOption());

		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
		
		return agentInfo;
	}

}
