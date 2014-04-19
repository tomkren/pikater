package org.pikater.core.options.xmlGenerators;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.options.LogicalBoxDescription;

public class RandomTree_CABox extends LogicalBoxDescription {
	public RandomTree_CABox(){
		super("RandomTree",ComputingAgent.class,"Random Tree Method");
		this.setPicture("picture3.jpg");
		this.setAgentName(Agent_WekaCA.class);
		
		/**
		# Sets the number of randomly chosen attributes.
		$ K int 1 1 r 1 50
		**/
		OptionDefault optionK = new OptionDefault();
		optionK.setName("K");
		optionK.setDescription("Sets the number of randomly chosen attributes");
		optionK.setValue(
				new OptionValue(new Integer(1)) );
		optionK.setInterval(
				new OptionInterval(new Integer(1), new Integer(50)) );
		optionK.setList( new OptionList() );
		
		
		/**
		# The minimum total weight of the instances in a leaf.
		$ M int 1 1 r 0 100
		**/
		OptionDefault optionM = new OptionDefault();
		optionM.setName("M");
		optionM.setDescription("The minimum total weight of the instances in a leaf");
		optionM.setValue(
				new OptionValue(new Integer(0)) );
		optionM.setInterval(
				new OptionInterval(new Integer(1), new Integer(100)) );
		optionM.setList( new OptionList() );
		
		
		/**
		# The random number seed used for selecting attributes.
		$ Q int 1 1 r 1 MAXINT
		**/
		OptionDefault optionQ = new OptionDefault();
		optionQ.setName("Q");
		optionQ.setDescription("The random number seed used for selecting attributes");
		optionQ.setValue(
				new OptionValue(new Integer(1)) );
		optionQ.setInterval(
				new OptionInterval(new Integer(1), new Integer(Integer.MAX_VALUE)) );
		optionQ.setList( new OptionList() );
		
		
		
		this.addParameter(optionK);
		this.addParameter(optionM);
		this.addParameter(optionQ);
	}
}
