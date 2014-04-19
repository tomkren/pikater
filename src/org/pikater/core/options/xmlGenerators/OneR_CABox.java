package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class OneR_CABox extends LogicalBoxDescription {
	public OneR_CABox(){
		super("OneR",ComputingAgent.class,"One R Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
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
		
		
		this.addOption(optionB);



		// Slots Definition
		this.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		this.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	}
}
