package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class Perceptron_CABox {

	public static AgentInfo get() {

		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/	
		NewOption optionL = new NewOption("L", new FloatValue(0.3f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionL.setDescription("Learning rate");
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		NewOption optionN = new NewOption("N", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(1000))
		);
		optionN.setDescription("Number of epochs to train through");
		
		
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		NewOption optionS = new NewOption("S", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionS.setDescription("Seed of the random number generator");
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		NewOption optionC = new NewOption("C", new BooleanValue(false));
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaPerceptronCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("Perceptron");
		agentInfo.setDescription("Perceptron Method");

		agentInfo.addOption(optionL);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionC);

		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
		
		return agentInfo;
	}

}
