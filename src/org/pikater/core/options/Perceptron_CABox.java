package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class Perceptron_CABox {

	public static AgentInfo get() {

		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/	
		Type typeL = new Type(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionL = new PossibleTypesRestriction();
		restrictionL.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(0.3f),
				new Type(FloatValue.class),
				"L" );
		optionL.setDescription("Learning rate");
		optionL.setPossibleTypesRestriction(restrictionL);
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		Type typeN = new Type(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of epochs to train through");
		optionN.setPossibleTypesRestriction(restrictionN);
		
		
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		Type typeS = new Type(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionS = new PossibleTypesRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new Type(IntegerValue.class),
				"S" );
		optionS.setDescription("Seed of the random number generator");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		Type typeC = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"C" );
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setPossibleTypesRestriction(restrictionC);

		
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaPerceptronCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("Perceptron");
		agentInfo.setDescription("Perceptron Method");

		agentInfo.addOption(optionL);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionC);

		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
		
		return agentInfo;
	}

}
