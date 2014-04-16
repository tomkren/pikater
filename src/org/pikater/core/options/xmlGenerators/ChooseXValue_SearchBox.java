package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_ChooseXValues;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
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

		this.addParameter(optionN);
	}
}
