package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_GridSearch;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;


public class GridSearch_SearchBox extends LogicalBoxDescription {
	protected GridSearch_SearchBox() {
		super("GridSearch",
				Search.class,
				"GridSearch Description"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_GridSearch.class);
		
	
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
		
		
		this.addOption(optionB);
		this.addOption(optionN);
		this.addOption(optionZ);
		
		

		// Slot Definition
		this.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
	}
}
