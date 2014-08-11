package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNNgeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class NNgeCA_Box {
	
	public static AgentInfo get() {

		/**
		# Set the number of folder to use in the computing of the mutual information (default 5)
		$ I int 1 1 r 1 100 
		**/
		NewOption optionI = new NewOption("I", new IntegerValue(5), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		);
		optionI.setDescription("Set the number of folder to use in the computing of the mutual information");
		
		/**
		# Set the number of attempts of generalisation (default 5)
		$ G int 1 1 r 1 50
		**/
		NewOption optionG = new NewOption("G", new IntegerValue(5), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(50))
		);
		optionG.setDescription("Set the number of attempts of generalisation");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaNNgeCA.class); // some virtual-box provider agent
		agentInfo.importOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("NNge");
		agentInfo.setDescription("NNge Method");

		agentInfo.addOption(optionI);
		agentInfo.addOption(optionG);
		agentInfo.addOptions(OptionsHelper.getCAOptions());
		agentInfo.addOptions(OptionsHelper.getCAorRecommenderOptions());

		//Slot Definition
		agentInfo.setInputSlots(SlotsHelper.getSlots_CAInput());
		agentInfo.setOutputSlots(SlotsHelper.getSlots_CAOutput());

		return agentInfo;
	}
	
}
