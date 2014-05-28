package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;

public class NaiveBayes_CABox {

	public static AgentInfo get() {

		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		OptionDefault optionK = new OptionDefault();
		optionK.setName("K");
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		optionK.setValue(
				new OptionValue(new Boolean(false)) );
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		OptionDefault optionD = new OptionDefault();
		optionD.setName("D");
		optionD.setDescription("Use supervised discretization to process numeric attributes");
		optionD.setValue(
				new OptionValue(new Boolean(false)) );


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaNaiveBayesCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NaiveBayes");
		agentInfo.setPicture("picture5.jpg");
		agentInfo.setDescription("Naive Bayes Method");

		agentInfo.addOption(optionK.toOption());
		agentInfo.addOption(optionD.toOption());
		
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
