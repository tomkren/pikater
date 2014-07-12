package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaNNgeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

public class NNge_CABox {
	
	public static AgentInfo get() {

		/**
		# Set the number of folder to use in the computing of the mutual information (default 5)
		$ I int 1 1 r 1 100 
		**/
		ValueType typeI = new ValueType(IntegerValue.class);
		typeI.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestrictions restrictionI = new TypeRestrictions();
		restrictionI.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new IntegerValue(5),
				new ValueType(IntegerValue.class),
				"I" );
		optionI.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionI.setTypeRestrictions(restrictionI);
		
		/**
		# Set the number of attempts of generalisation (default 5)
		$ G int 1 1 r 1 50
		**/
		ValueType typeG = new ValueType(IntegerValue.class);
		typeG.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(50) ));
		TypeRestrictions restrictionG = new TypeRestrictions();
		restrictionG.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new IntegerValue(5),
				new ValueType(IntegerValue.class),
				"G" );
		optionG.setDescription("Set the number of attempts of generalisation");
		optionG.setTypeRestrictions(restrictionG);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaNNgeCA.class); // some virtual-box provider agent
		agentInfo.setOntologyClass(FileDataProvider.class);
	
		agentInfo.setName("NNge");
		agentInfo.setDescription("NNge Method");

		agentInfo.addOption(optionI);
		agentInfo.addOption(optionG);


		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}
	
}
