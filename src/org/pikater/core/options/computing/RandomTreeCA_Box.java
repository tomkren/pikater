package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaRandomTreeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.options.AgentDefinitionHelper;

public class RandomTreeCA_Box {
	
	public static AgentInfo get() {

		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		NewOption optionK = new NewOption("K", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(50))
		);
		optionK.setDescription("Sets the number of randomly chosen attributes");
		
		
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		NewOption optionM = new NewOption("M", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(100))
		);
		optionM.setDescription("The minimum total weight of the instances in a leaf");
		
		
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		NewOption optionQ = new NewOption("Q", new IntegerValue(0), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionQ.setDescription("The random number seed used for selecting attributes");


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaRandomTreeCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("RandomTree");
		agentInfo.setDescription("Random Tree Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionQ);
		agentInfo.addOptions(AgentDefinitionHelper.getCAOptions());
		
		// Slots Definition
		agentInfo.setInputSlots(AgentDefinitionHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AgentDefinitionHelper.getCAOutputSlots());

		return agentInfo;
	}

}
