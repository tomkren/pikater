package org.pikater.core.options;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;

public class SimulatedAnnealing_SearchBox {

	public static AgentInfo get() {

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
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_SimulatedAnnealing.class.toString());
		agentInfo.setOntologyClass(Search.class.getName());
	
		agentInfo.setName("SimulatedAnnealing-Searcher");
		agentInfo.setPicture("picture2.jpg");
		agentInfo.setDescription("Searches defined parameters and provides them in output slots. Simulated annealing is used for searching.");

		agentInfo.addOption(optionE.toOption());
		agentInfo.addOption(optionM.toOption());
		agentInfo.addOption(optionT.toOption());
		agentInfo.addOption(optionS.toOption());

		// Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}