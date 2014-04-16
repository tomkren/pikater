package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_EASearch;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalBoxDescription;


public class EASearch_SearchBox extends LogicalBoxDescription {
	
	protected EASearch_SearchBox() {
		super("EASearch",
				Search.class,
				"Searcher using Evolution algorithm"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_EASearch.class);
			
		OptionValue valueE = new OptionValue(new Float(0.1f));
		OptionInterval intervalE = new OptionInterval(new Float(0.0f), new Float(1.0f));
		OptionList listE = new OptionList();
		
		OptionDefault optionE = new OptionDefault(valueE, intervalE, listE);
		optionE.setName("E");
		optionE.setDescription("Minimum error rate");

		
		OptionValue valueM = new OptionValue(new Integer(10));
		OptionInterval intervalM = new OptionInterval(new Integer(1), new Integer(1000));
		OptionList listM = new OptionList();
		
		OptionDefault optionM = new OptionDefault(valueE, intervalE, listE);
		optionM.setName("M");
		optionM.setDescription("Maximal number of generations");
		
		
		OptionValue valueT = new OptionValue(new Float(0.2f));
		OptionInterval intervalT = new OptionInterval(new Float(0.0f), new Float(1.0f));
		OptionList listT = new OptionList();
		
		OptionDefault optionT = new OptionDefault(valueT, intervalT, listT);
		optionT.setName("T");
		optionT.setDescription("Mutation rate");

		
		OptionValue valueX = new OptionValue(new Float(0.5f));
		OptionInterval intervalX = new OptionInterval(new Float(0.0f), new Float(1.0f));
		OptionList listX = new OptionList();
		
		OptionDefault optionX = new OptionDefault(valueX, intervalX, listX);
		optionX.setName("X");
		optionX.setDescription("Crossover probability");

	
		OptionValue valueP = new OptionValue(new Integer(10));
		OptionInterval intervalP = new OptionInterval(new Integer(1), new Integer(100));
		OptionList listP = new OptionList();
		
		OptionDefault optionP = new OptionDefault(valueP, intervalP, listP);
		optionP.setName("P");
		optionP.setDescription("Population size");


		OptionValue valueI = new OptionValue(new Integer(10));
		OptionInterval intervalI = new OptionInterval(new Integer(1), new Integer(100));
		OptionList listI = new OptionList();
		
		OptionDefault optionI = new OptionDefault(valueI, intervalI, listI);
		optionI.setName("I");
		optionI.setDescription("Maximum number of option evaluations");


		OptionValue valueF = new OptionValue(new Float(0.2f));
		OptionInterval intervalF = new OptionInterval(new Float(0.0f), new Float(1.0f));
		OptionList listF = new OptionList();
		
		OptionDefault optionF = new OptionDefault(valueF, intervalF, listF);
		optionF.setName("F");
		optionF.setDescription("Mutation rate per field in individual");


		OptionValue valueL = new OptionValue(new Float(0.1f));
		OptionInterval intervalL = new OptionInterval(new Float(0.0f), new Float(1.0f));
		OptionList listL = new OptionList();
		
		OptionDefault optionL = new OptionDefault(valueL, intervalL, listL);
		optionL.setName("L");
		optionL.setDescription("The percentage of elite individuals");

		
		this.addParameter(optionE);
		this.addParameter(optionM);
		this.addParameter(optionT);
		this.addParameter(optionX);
		this.addParameter(optionP);
		this.addParameter(optionI);
		this.addParameter(optionF);
		this.addParameter(optionL);
	}
}
