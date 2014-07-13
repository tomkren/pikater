package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;

public class NaiveBayes_CABox {

	public static AgentInfo get() {

		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		NewOption optionK = new NewOption("K", new BooleanValue(false));
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		NewOption optionD = new NewOption("D", new BooleanValue(false));
		optionD.setDescription("Use supervised discretization to process numeric attributes");


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaNaiveBayesCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NaiveBayes");
		agentInfo.setDescription("Naive Bayes Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionD);
		
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
