package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaNNgeCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class NNge_CABox {
	
	public static AgentInfo get() {

		/**
		# Set the number of folder to use in the computing of the mutual information (default 5)
		$ I int 1 1 r 1 100 
		**/
		Type typeI = new Type(IntegerValue.class);
		typeI.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		PossibleTypesRestriction restrictionI = new PossibleTypesRestriction();
		restrictionI.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new IntegerValue(5),
				new Type(IntegerValue.class),
				"I" );
		optionI.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionI.setPossibleTypesRestriction(restrictionI);
		
		/**
		# Set the number of attempts of generalisation (default 5)
		$ G int 1 1 r 1 50
		**/
		Type typeG = new Type(IntegerValue.class);
		typeG.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(50) ));
		PossibleTypesRestriction restrictionG = new PossibleTypesRestriction();
		restrictionG.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeG )) ));
		
		NewOption optionG = new NewOption(
				new IntegerValue(5),
				new Type(IntegerValue.class),
				"G" );
		optionG.setDescription("Set the number of attempts of generalisation");
		optionG.setPossibleTypesRestriction(restrictionG);
		

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
