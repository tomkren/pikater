package org.pikater.core.options.computing;

import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaPARTCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;
import org.pikater.core.options.AAA_SlotHelper;

public class PARTCA_Box {
	
	public static AgentInfo get() {

		/**
		# Set confidence threshold for pruning. (Default: 0.25)
		# $ C float 1 1 r 0.0001 0.4 
		$ C float 1 1 s null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4, 0.5
		**/
		NewOption optionC = new NewOption("C", new FloatValue(0.25f), new SetRestriction(Arrays.asList(
				new NullValue(), new FloatValue(0.0001f),
				new FloatValue(0.1f), new FloatValue(0.2f),
				new FloatValue(0.25f), new FloatValue(0.3f),
				new FloatValue(0.4f), new FloatValue(0.5f)))
		);
		optionC.setDescription("Set the number of folder to use in the computing of the mutual information");
		
		
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		NewOption optionM = new NewOption("M", new IntegerValue(2), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10))
		);
		optionM.setDescription("Set minimum number of instances per leaf");
		
		
		/**
		# Use reduced error pruning.
		$ R boolean
		**/
		NewOption optionR = new NewOption("R", new BooleanValue(false));
		optionR.setDescription("Use reduced error pruning");

		
		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		NewOption optionN = new NewOption("N", new IntegerValue(3), new SetRestriction(Arrays.asList(
				new NullValue(), new IntegerValue(1),
				new IntegerValue(2), new IntegerValue(3),
				new IntegerValue(4), new IntegerValue(5)
		)));
		optionN.setDescription("Set the number of folder to use in the computing of the mutual information");


		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		NewOption optionB = new NewOption("B", new BooleanValue(false));
		optionB.setDescription("Use reduced error pruning");


		/**
		# Generate unpruned decision list.
		$ U boolean
		**/
		NewOption optionU = new NewOption("U", new BooleanValue(false));
		optionU.setDescription("Generate unpruned decision list");
		
		
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		NewOption optionQ = new NewOption("Q", new IntegerValue(1), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionQ.setDescription("The seed for reduced-error pruning");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaPARTCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("PART");
		agentInfo.setDescription("PART Method");

		agentInfo.addOption(optionC);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionU);
		agentInfo.addOption(optionQ);

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
