package org.pikater.core.options.computing;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaSMOregCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.options.AAA_SlotHelper;

public class SMORegCA_Box  {
	
	public static AgentInfo get() {

		/**
		# -S num
		# The amount up to which deviation are tolerated (epsilon). (default 1e-3)
		# Watch out, the value of epsilon is used with the (normalized/standardize) data
		**/
		NewOption optionS = new NewOption("S", new FloatValue(1e-3f));
		optionS.setDescription("The amount up to which deviation are tolerated (epsilon)");
		
		
		/**
		# -C num
		# The complexity constant C. (default 1)
		**/
		NewOption optionC = new NewOption("C", new IntegerValue(1));
		optionC.setDescription("The complexity constant");
		

		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		NewOption optionE = new NewOption("E", new IntegerValue(1));
		optionE.setDescription("The exponent for the polynomial kernel");
		
		
		/**
		# -G num
		# Gamma for the RBF kernel. (default 0.01)
		**/
		NewOption optionG = new NewOption("G", new FloatValue(0.01f));
		optionG.setDescription("Gamma for the RBF kernel");
		
		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		NewOption optionN = new NewOption("N", new IntegerValue(1), new SetRestriction(new ArrayList<IValueData>(Arrays.asList(
				new IntegerValue(0),
				new IntegerValue(1),
				new IntegerValue(2))))
		);
		optionN.setDescription("Random number seed for cross-validation");
		
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		NewOption optionF = new NewOption("F", new BooleanValue(false));
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		
		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		NewOption optionO = new NewOption("O", new BooleanValue(false));
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		
		
		/**
		# Use RBF kernel (default poly).
		$ R boolean
		**/
		NewOption optionR = new NewOption("R", new BooleanValue(false));
		optionR.setDescription("Use RBF kernel (default poly)");
		
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/
		NewOption optionA = new NewOption("A", new IntegerValue(250007), new RangeRestriction(
				new IntegerValue(0),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		NewOption optionP = new NewOption("P", new FloatValue(1.0e-12f));
		optionP.setDescription("Sets the epsilon for round-off error");
		
		
		/**
		# -T num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		NewOption optionT = new NewOption("T", new FloatValue(1.0e-3f));
		optionT.setDescription("Sets the epsilon for round-off error");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaSMOregCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("SMO Reg");
		agentInfo.setDescription("SMOReg Method");

		agentInfo.addOption(optionS);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionE);
		agentInfo.addOption(optionG);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionF);
		agentInfo.addOption(optionO);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionT);

		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
