package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_RandomSearch;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalBoxDescription;

public class RandomSearch_SearchBox extends LogicalBoxDescription {

	protected RandomSearch_SearchBox() {
		super("Random-Searcher",
				Search.class,
				"Selects and provides random values for its output parameters."
				);

		this.setPicture("picture1.jpg");
		this.setAgentName(Agent_RandomSearch.class);


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
		
		this.addParameter(optionE);
		this.addParameter(optionM);
	}
}