package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaOneRCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;

public class OneR_CABox {
	

	public static AgentInfo get() {

		/**
		# Specify the minimum number of objects in a bucket (default: 6).
		$ B int 1 1 r 1 100
		**/
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("Specify the minimum number of objects in a bucket");
		optionB.setValue(
				new OptionValue(new Integer(6)) );
		optionB.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionB.setList(
				new OptionList() );
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaOneRCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("OneR");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("One R Method");

		agentInfo.addOption(optionB.toOption());
		

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
