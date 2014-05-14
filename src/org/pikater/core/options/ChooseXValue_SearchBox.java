package org.pikater.core.options;

import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.Search;

public class ChooseXValue_SearchBox {
	
	public static AgentInfo get() {

		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Number of values to try for each option");
		optionN.setValue(
				new OptionValue(new Integer(5)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(2000)) );
		optionN.setList(
				new OptionList() );

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(null); // some virtual-box provider agent
		agentInfo.setOntologyClass(Search.class.getName());
	
		agentInfo.setName("Choose X Values Agent");
		agentInfo.setPicture("picture0.jpg");
		agentInfo.setDescription("Search which Choose X Values");

		agentInfo.addOption(optionN.toOption());
		
		// Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());

		return agentInfo;
	}

}
