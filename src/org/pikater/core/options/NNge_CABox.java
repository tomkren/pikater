package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaNNgeCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;

public class NNge_CABox {
	
	public static AgentInfo get() {

		/**
		# Set the number of folder to use in the computing of the mutual information (default 5)
		$ I int 1 1 r 1 100 
		**/
		OptionDefault optionI = new OptionDefault();
		optionI.setName("I");
		optionI.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionI.setValue(
				new OptionValue(new Integer(5)) );
		optionI.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionI.setList( new OptionList() );
		
		/**
		# Set the number of attempts of generalisation (default 5)
		$ G int 1 1 r 1 50
		**/
		OptionDefault optionG = new OptionDefault();
		optionG.setName("G");
		optionG.setDescription("Set the number of attempts of generalisation");
		optionG.setValue(
				new OptionValue(new Integer(5)) );
		optionG.setInterval(
				new OptionInterval(new Integer(1), new Integer(50)) );
		optionG.setList( new OptionList() );
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaNNgeCA.class); // some virtual-box provider agent
		agentInfo.setOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("NNge");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("NNge Method");

		agentInfo.addOption(optionI.toOption());
		agentInfo.addOption(optionG.toOption());


		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}
	
}
