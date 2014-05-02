package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.description.ComputingAgent;

public class RBFNetwork_CABox {
	
	public static AgentInfo get() {

		/**
		# number of clusters, default 2
		$ B int 1 1 r 2 1000
		**/
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("Number of clusters");
		optionB.setValue(
				new OptionValue(new Integer(2)) );
		optionB.setInterval(
				new OptionInterval(new Integer(2), new Integer(1000)) );
		optionB.setList( new OptionList() );
		
		/**
		# minStdDev, default 0.1
		$ W float 1 1 r 0.01 2
		**/		
		OptionDefault optionW = new OptionDefault();
		optionW.setName("W");
		optionW.setDescription("minStdDev");
		optionW.setValue(
				new OptionValue(new Float(0.1f)) );
		optionW.setInterval(
				new OptionInterval(new Float(0.01f), new Float(2.0f)) );
		optionW.setList( new OptionList() );
		
		
		/**
		# Ridge (Set the Ridge value for the logistic or linear regression), default 1.0E-8
		$ R float 1 1 r 0.000000001 10
		**/
		OptionDefault optionR = new OptionDefault();
		optionR.setName("R");
		optionR.setDescription("Ridge (Set the Ridge value for the logistic or linear regression)");
		optionR.setValue(
				new OptionValue(new Float(1.0e-8f)) );
		optionR.setInterval(
				new OptionInterval(new Float(0.000000001f), new Float(10.0f)) );
		optionR.setList( new OptionList() );
		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("The value used to seed the random number generator");
		optionS.setValue(
				new OptionValue(new Integer(0)) );
		optionS.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		optionS.setList( new OptionList() );
		
		/**
		#  Set the maximum number of iterations for the logistic regression. (default -1, until convergence).
		$ M int 1 1 r -1 50
		**/		
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Set the maximum number of iterations for the logistic regression");
		optionM.setValue(
				new OptionValue(new Integer(-1)) );
		optionM.setInterval(
				new OptionInterval(new Integer(-1), new Integer(50)) );
		optionM.setList( new OptionList() );



		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaCA.class.getName());
		agentInfo.setOntologyClass(ComputingAgent.class.getName());

		agentInfo.setName("RBFNetwork");
		agentInfo.setPicture("picture3.jpg");
		agentInfo.setDescription("RBFNetwork Method");

		agentInfo.addOption(optionB.toOption());
		agentInfo.addOption(optionW.toOption());
		agentInfo.addOption(optionR.toOption());
		agentInfo.addOption(optionS.toOption());
		agentInfo.addOption(optionM.toOption());

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
