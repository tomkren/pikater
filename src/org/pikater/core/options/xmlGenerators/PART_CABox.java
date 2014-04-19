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

public class PART_CABox extends LogicalBoxDescription {
	public PART_CABox(){
		super("PART",ComputingAgent.class,"PART Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# Set confidence threshold for pruning. (Default: 0.25)
		# $ C float 1 1 r 0.0001 0.4 
		$ C float 1 1 s null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4, 0.5
		**/		
		OptionDefault optionC = new OptionDefault();
		optionC.setName("C");
		optionC.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionC.setValue(
				new OptionValue(new Float(0.25f)) );
		optionC.setInterval(
				null );
		OptionList listC = new OptionList();
		listC.setList(
				new ArrayList<Object>(
						Arrays.asList(new Float[] {null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f,0.5f})
						) );
		optionC.setList( listC );
		
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("Set minimum number of instances per leaf");
		optionM.setValue(
				new OptionValue(new Integer(2)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(10)) );
		optionM.setList( new OptionList() );
		
		
		/**
		# Use reduced error pruning.
		$ R boolean
		**/		
		OptionDefault optionR = new OptionDefault();
		optionR.setName("R");
		optionR.setDescription("Use reduced error pruning");
		optionR.setValue(
				new OptionValue(new Boolean(false)) );


		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		OptionDefault optionN = new OptionDefault();
		optionN.setName("N");
		optionN.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionN.setValue(
				new OptionValue(new Integer(3)) );
		OptionList listN = new OptionList();
		listN.setList(
				new ArrayList<Object>(
						Arrays.asList(new Integer[] {null,1,2,3,4,5})
						) );
		optionC.setList( listN );


		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		OptionDefault optionB = new OptionDefault();
		optionB.setName("B");
		optionB.setDescription("Use reduced error pruning");
		optionB.setValue(
				new OptionValue(new Boolean(false)) );


		/**
		# Generate unpruned decision list.
		$ U boolean
		**/
		OptionDefault optionU = new OptionDefault();
		optionU.setName("U");
		optionU.setDescription("Generate unpruned decision list");
		optionU.setValue(
				new OptionValue(new Boolean(false)) );


		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/		
		OptionDefault optionQ = new OptionDefault();
		optionQ.setName("Q");
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setValue(
				new OptionValue(new Integer(1)) );
		optionQ.setInterval(
				new OptionInterval(new Integer(1), new Integer(Integer.MAX_VALUE)) );
		optionQ.setList( new OptionList() );
		
		
		this.addParameter(optionC);
		this.addParameter(optionM);
		this.addParameter(optionR);
		this.addParameter(optionN);
		this.addParameter(optionB);
		this.addParameter(optionU);
		this.addParameter(optionQ);
		
		
	}
}
