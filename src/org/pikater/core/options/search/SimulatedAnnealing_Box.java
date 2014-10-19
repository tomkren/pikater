package org.pikater.core.options.search;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.Search;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.SlotsHelper;

public class SimulatedAnnealing_Box {

	public static AgentInfo get() {

		NewOption optionE = new NewOption("E", new DoubleValue(0.1), new RangeRestriction(
				new DoubleValue(0.0),
				new DoubleValue(1.0))
		);
		optionE.setDescription("Set minimum number of instances per leaf");
		
		
		NewOption optionM = new NewOption("M", new IntegerValue(50), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(1000))
		);
		optionM.setDescription("M");
		

		NewOption optionT = new NewOption("T", new DoubleValue(1.0), new RangeRestriction(
				new DoubleValue(0.0),
				new DoubleValue(100.0))
		);
		optionT.setDescription("T");
		
		
		NewOption optionS = new NewOption("S", new DoubleValue(0.5), new RangeRestriction(
				new DoubleValue(0.0),
				new DoubleValue(1.0))
		);
		optionS.setDescription("T");
		
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_SimulatedAnnealing.class);
		agentInfo.importOntologyClass(Search.class);
	
		agentInfo.setName("SimulatedAnnealing");
		agentInfo.setDescription("Searches defined parameters and provides them in output slots. Simulated annealing is used for searching.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionS);

		// Slot Definition
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_Search());		

		return agentInfo;
	}

}