package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.AAA_SlotHelper;

public class RBFNetworkCA_Box {
	
	public static AgentInfo get() {

		/**
		# number of clusters, default 2
		$ B int 1 1 r 2 1000
		**/
		NewOption optionB = new NewOption("B", new IntegerValue(2), new RangeRestriction(
				new IntegerValue(2),
				new IntegerValue(1000))
		);
		optionB.setDescription("Number of clusters");
		
		
		/**
		# minStdDev, default 0.1
		$ W float 1 1 r 0.01 2
		**/
		NewOption optionW = new NewOption("W", new FloatValue(0.25f), new RangeRestriction(
				new FloatValue(0.01f),
				new FloatValue(2.0f))
		);
		optionW.setDescription("minStdDev");
		
		
		/**
		# Ridge (Set the Ridge value for the logistic or linear regression), default 1.0E-8
		$ R float 1 1 r 0.000000001 10
		**/
		NewOption optionR = new NewOption("R", new FloatValue(1.0e-8f), new RangeRestriction(
				new FloatValue(0.000000001f),
				new FloatValue(10.0f))
		);
		optionR.setDescription("Ridge (Set the Ridge value for the logistic or linear regression)");
		
		
		/**
		#  The value used to seed the random number generator
		#  (Value should be >= 0 and and a long, Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		NewOption optionS = new NewOption("S", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionS.setDescription("The value used to seed the random number generator");
		
		
		/**
		#  Set the maximum number of iterations for the logistic regression. (default -1, until convergence).
		$ M int 1 1 r -1 50
		**/
		NewOption optionM = new NewOption("M", new IntegerValue(-1), new RangeRestriction(
				new IntegerValue(-1),
				new IntegerValue(50))
		);
		optionM.setDescription("Set the maximum number of iterations for the logistic regression");


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaRBFNetworkCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);

		agentInfo.setName("RBFNetwork");
		agentInfo.setDescription("RBFNetwork Method");

		agentInfo.addOption(optionB);
		agentInfo.addOption(optionW);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionM);
		agentInfo.addOptions(AAA_SlotHelper.getCAOptions());
		
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
