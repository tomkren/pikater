package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class SMOReg_CABox extends LogicalBoxDescription {
	public SMOReg_CABox(){
		super("SMO Reg",ComputingAgent.class,"SMOReg Method");
		this.setAgentName(Agent_WekaCA.class);
		this.setPicture("picture3.jpg");
		
		/**
		# -S num
		# The amount up to which deviation are tolerated (epsilon). (default 1e-3)
		# Watch out, the value of epsilon is used with the (normalized/standardize) data
		**/
		OptionDefault optionS = new OptionDefault();
		optionS.setName("S");
		optionS.setDescription("The amount up to which deviation are tolerated (epsilon)");
		optionS.setValue( new OptionValue(new Float(1e-3f)) );
		
		/**
		# -C num
		# The complexity constant C. (default 1)
		**/
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("The complexity constant");
		optionC.setValue( new OptionValue(new Integer(1)) );

		/**
		# -E num
		# The exponent for the polynomial kernel. (default 1)
		**/
		OptionDefault optionE = new OptionDefault();
		optionE.setName("E");
		optionE.setDescription("The exponent for the polynomial kernel");
		optionE.setValue( new OptionValue(new Integer(1)) );
		
		/**
		# -G num
		# Gamma for the RBF kernel. (default 0.01)
		**/
		OptionDefault optionG = new OptionDefault();
		optionG.setName("G");
		optionG.setDescription("Gamma for the RBF kernel");
		optionG.setValue( new OptionValue(new Float(0.01f)) );
		
		/**
		# Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
		$ N int 1 1 s 0, 1, 2
		**/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Random number seed for cross-validation");
		optionN.setValue( new OptionValue(new Integer(1)) );
		OptionList listN = new OptionList();
		listN.setList(
				new ArrayList<Object>(Arrays.asList(new Integer[] {0,1,2}))
		);
		optionN.setList(listN);
		
		/**
		# Feature-space normalization (only for non-linear polynomial kernels).
		$ F boolean
		**/
		OptionDefault optionF = new OptionDefault();
		optionF.setName("F");
		optionF.setDescription("Feature-space normalization (only for non-linear polynomial kernels)");
		optionF.setValue( new OptionValue(new Boolean(false)) );
		
		/**
		# Use lower-order terms (only for non-linear polynomial kernels).
		$ O boolean
		**/
		OptionDefault optionO = new OptionDefault();
		optionO.setName("O");
		optionO.setDescription("Use lower-order terms (only for non-linear polynomial kernels)");
		optionO.setValue( new OptionValue(new Boolean(false)) );

		
		/**
		# Use RBF kernel (default poly).
		$ R boolean
		**/
		OptionDefault optionR = new OptionDefault();
		optionR.setName("R");
		optionR.setDescription("Use RBF kernel (default poly)");
		optionR.setValue( new OptionValue(new Boolean(false)) );
		
		/**
		# Sets the size of the kernel cache. Should be a prime number. (default 250007, use 0 for full cache)
		$ A int 1 1 r 0 MAXINT
		**/
		OptionDefault optionA = new OptionDefault();
		optionA.setName("A");
		optionA.setDescription("Sets the size of the kernel cache. Should be a prime number");
		optionA.setValue( new OptionValue(new Integer(250007)) );
		optionA.setInterval(
				new OptionInterval(new Integer(0), new Integer(Integer.MAX_VALUE)) );
		
		/**
		# -P num
		# Sets the epsilon for round-off error. (default 1.0e-12)
		**/
		OptionDefault optionP = new OptionDefault();
		optionP.setName("P");
		optionP.setDescription("Sets the epsilon for round-off error");
		optionP.setValue( new OptionValue(new Float(1.0e-12f)) );
		
		/**
		# -T num
		# Sets the tolerance parameter. (default 1.0e-3)
		**/
		OptionDefault optionT = new OptionDefault();
		optionT.setName("T");
		optionT.setDescription("Sets the epsilon for round-off error");
		optionT.setValue( new OptionValue(new Float(1.0e-3f)) );
		
		this.addOption(optionS);
		this.addOption(optionC);
		this.addOption(optionE);
		this.addOption(optionG);
		this.addOption(optionN);
		this.addOption(optionF);
		this.addOption(optionO);
		this.addOption(optionR);
		this.addOption(optionA);
		this.addOption(optionP);
		this.addOption(optionT);


		// Slots Definition
		this.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		this.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	}
}
