package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_GASearch;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;

public class GASearch_SearchBox extends LogicalBoxDescription {
	protected GASearch_SearchBox() {
		super("GASearch",
				Search.class,
				"Searcher using Genetic algorithm"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_GASearch.class);		
		
		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("Minimum error rate");
		optionE.setValue(
				new OptionValue(new Float(0.1f)) );
		optionE.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1.0f)) );
		optionE.setList(
				new OptionList() );

		
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Maximal number of generations");
		optionM.setValue(
				new OptionValue(new Integer(10)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(1000)) );
		optionM.setList(
				new OptionList() );
		
		OptionDefault optionT = new OptionDefault();
		optionT.setName("T");
		optionT.setDescription("Mutation rate");
		optionT.setValue(
				new OptionValue(new Float(0.2f)) );
		optionT.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1.0f)) );
		optionT.setList(
				new OptionList() );
		
		OptionDefault optionX = new OptionDefault();
		optionX.setName("X");
		optionX.setDescription("Crossover probability");
		optionX.setValue(
				new OptionValue(new Float(0.5f)) );
		optionX.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1.0f)) );
		optionX.setList(
				new OptionList() );

		OptionDefault optionP = new OptionDefault();
		optionP.setName("P");
		optionP.setDescription("Population size");		
		optionP.setValue(
				new OptionValue(new Integer(5)) );
		optionP.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionP.setList(
				new OptionList() );

	
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("Size of tournament in selection");		
		optionS.setValue(
				new OptionValue(new Integer(2)) );
		optionS.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionS.setList(
				new OptionList() );

		
		this.addOption(optionE);
		this.addOption(optionM);
		this.addOption(optionT);
		this.addOption(optionX);
		this.addOption(optionP);
		this.addOption(optionS);



		// Slot Definition
		this.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
	}
}
