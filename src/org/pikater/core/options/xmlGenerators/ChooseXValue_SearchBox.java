package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.dataStructures.slots.Slot;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;

public class ChooseXValue_SearchBox extends LogicalBoxDescription {
	
	protected ChooseXValue_SearchBox() {
		super(
			"Choose X Values Agent",
		        Search.class,
		        "Search which Choose X Values"
		        );

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_ChooseXValues.class);

		
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Number of values to try for each option");
		optionN.setValue(
				new OptionValue(new Integer(5)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(2000)) );
		optionN.setList(
				new OptionList() );

		this.addOption(optionN);



		// Slot Definition
		this.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
	}
}
