package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaRandomTreeCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;

public class RandomTree_CABox {
	
	public static AgentInfo get() {

		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		OptionDefault optionK = new OptionDefault();
		optionK.setName("K");
		optionK.setDescription("Sets the number of randomly chosen attributes");
		optionK.setValue(
				new OptionValue(new Integer(1)) );
		optionK.setInterval(
				new OptionInterval(new Integer(1), new Integer(50)) );
		optionK.setList( new OptionList() );
		
		
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("The minimum total weight of the instances in a leaf");
		optionM.setValue(
				new OptionValue(new Integer(0)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionM.setList( new OptionList() );
		
		
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		OptionDefault optionQ = new OptionDefault();
		optionQ.setName("Q");
		optionQ.setDescription("The random number seed used for selecting attributes");
		optionQ.setValue(
				new OptionValue(new Integer(1)) );
		optionQ.setInterval(
				new OptionInterval(new Integer(1), new Integer(Integer.MAX_VALUE)) );
		optionQ.setList( new OptionList() );


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaRandomTreeCA.class.getName());
		agentInfo.setOntologyClass(ComputingAgent.class.getName());
	
		agentInfo.setName("RandomTree");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Random Tree Method");

		agentInfo.addOption(optionK.toOption());
		agentInfo.addOption(optionM.toOption());
		agentInfo.addOption(optionQ.toOption());

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
