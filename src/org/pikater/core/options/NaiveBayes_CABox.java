package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;

public class NaiveBayes_CABox {

	public static AgentInfo get() {

		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		ValueType typeK = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionK = new TypeRestrictions();
		restrictionK.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeK )) ));
		
		NewOption optionK = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"K" );
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		optionK.setTypeRestrictions(restrictionK);
		
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		ValueType typeD = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionD = new TypeRestrictions();
		restrictionD.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeD )) ));
		
		NewOption optionD = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"D" );
		optionD.setDescription("Use supervised discretization to process numeric attributes");
		optionD.setTypeRestrictions(restrictionD);
		



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
