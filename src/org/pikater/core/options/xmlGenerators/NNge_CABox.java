package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class NNge_CABox extends LogicalBoxDescription {
	public NNge_CABox(){
		super("NNge",ComputingAgent.class,"NNge Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
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
		
		
		this.addOption(optionI);
		this.addOption(optionG);



		// Slots Definition
		this.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		this.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	}
}
