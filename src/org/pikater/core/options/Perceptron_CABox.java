package org.pikater.core.options;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.description.ComputingAgent;

public class Perceptron_CABox {

	public static AgentInfo get() {

		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/	
		OptionDefault optionL = new OptionDefault();
		optionL.setName("L");
		optionL.setDescription("Learning rate");
		optionL.setValue(
				new OptionValue(new Float(0.3f)) );
		optionL.setInterval(
				new OptionInterval(new Float(0.0f), new Float(1.0f)) );
		optionL.setList( new OptionList() );
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Number of epochs to train through");
		optionN.setValue(
				new OptionValue(new Integer(1)) );
		optionN.setInterval(
				new OptionInterval(new Integer(1), new Integer(1000)) );
		optionN.setList( new OptionList() );
		
		
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/		
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("Seed of the random number generator");
		optionS.setValue(
				new OptionValue(new Integer(0)) );
		optionS.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		optionS.setList( new OptionList() );
		
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setValue(
				new OptionValue(new Boolean(false)) );


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaPerceptronCA.class.getName());
		agentInfo.setOntologyClass(ComputingAgent.class.getName());
	
		agentInfo.setName("Perceptron");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("Perceptron Method");

		agentInfo.addOption(optionL.toOption());
		agentInfo.addOption(optionN.toOption());
		agentInfo.addOption(optionS.toOption());
		agentInfo.addOption(optionC.toOption());

		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
		
		return agentInfo;
	}

}
