package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.options.AAA_SlotHelper;

public class NaiveBayesCA_Box {

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
		agentInfo.importAgentClass(Agent_WekaNaiveBayesCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NaiveBayes");
		agentInfo.setDescription("Naive Bayes Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionD);
		agentInfo.addOptions(AAA_SlotHelper.getCAOptions());
		
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
