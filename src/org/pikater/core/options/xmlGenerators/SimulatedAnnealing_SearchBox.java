package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.options.OptionDefault;
import org.pikater.core.ontology.options.types.OptionInterval;
import org.pikater.core.ontology.options.types.OptionList;
import org.pikater.core.ontology.options.types.OptionValue;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.util.Interval;

public class SimulatedAnnealing_SearchBox extends LogicalBoxDescription {

	protected SimulatedAnnealing_SearchBox() {
		super("SimulatedAnnealing-Searcher",
				Search.class,
				"Searches defined parameters and provides them in output slots. Simulated annealing is used for searching."
				);

		this.setPicture("picture2.jpg");
		this.setAgentName(Agent_SimulatedAnnealing.class);
		
		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("Set minimum number of instances per leaf");
		optionE.setValue(
				new OptionValue(new Double(0.1)) );
		optionE.setInterval(
				new OptionInterval(new Double(0.0), new Double(1.0)) );
		optionE.setList( new OptionList() );
		

		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("M");
		optionM.setValue(
				new OptionValue(new Integer(50)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(1000)) );
		optionM.setList( new OptionList() );
		

		OptionDefault optionT = new OptionDefault();
		optionT.setName("T");
		optionT.setDescription("T");
		optionT.setValue(
				new OptionValue(new Double(1.0)) );
		optionT.setInterval(
				new OptionInterval(new Double(0.0), new Double(100.0)) );
		optionT.setList( new OptionList() );

		
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("S");
		optionS.setValue(
				new OptionValue(new Double(0.5)) );
		optionS.setInterval(
				new OptionInterval(new Double(0.0), new Double(1.0)) );
		optionS.setList( new OptionList() );
		
		
		this.addParameter(optionE);
		this.addParameter(optionM);
		this.addParameter(optionT);
		this.addParameter(optionS);
		
	}
}