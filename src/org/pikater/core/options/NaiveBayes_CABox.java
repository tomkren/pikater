package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;

public class NaiveBayes_CABox {

	public static AgentInfo get() {

		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		Type typeK = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionK = new PossibleTypesRestriction();
		restrictionK.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeK )) ));
		
		NewOption optionK = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"K" );
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		optionK.setPossibleTypesRestriction(restrictionK);
		
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		Type typeD = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionD = new PossibleTypesRestriction();
		restrictionD.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeD )) ));
		
		NewOption optionD = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"D" );
		optionD.setDescription("Use supervised discretization to process numeric attributes");
		optionD.setPossibleTypesRestriction(restrictionD);
		



		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaNaiveBayesCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NaiveBayes");
		agentInfo.setPicture("picture5.jpg");
		agentInfo.setDescription("Naive Bayes Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionD);
		
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
