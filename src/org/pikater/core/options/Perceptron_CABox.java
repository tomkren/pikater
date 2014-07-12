package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaPerceptronCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;

public class Perceptron_CABox {

	public static AgentInfo get() {

		/**
		# learning rate, default 0.3; 1 arguments
		$ L float 1 1 r 0 1
		**/	
		ValueType typeL = new ValueType(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestrictions restrictionL = new TypeRestrictions();
		restrictionL.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(0.3f),
				new ValueType(FloatValue.class),
				"L" );
		optionL.setDescription("Learning rate");
		optionL.setTypeRestrictions(restrictionL);
		
		
		/**
		#  Number of epochs to train through.
		$ N int 1 1 r 1 1000
		 **/
		ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		TypeRestrictions restrictionN = new TypeRestrictions();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Number of epochs to train through");
		optionN.setTypeRestrictions(restrictionN);
		
		
		/**
		/**
		#  Seed of the random number generator (Default = 0).
		$ S int 1 1 r 0 MAXINT
		**/
		ValueType typeS = new ValueType(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(Integer.MAX_VALUE) ));
		TypeRestrictions restrictionS = new TypeRestrictions();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(0),
				new ValueType(IntegerValue.class),
				"S" );
		optionS.setDescription("Seed of the random number generator");
		optionS.setTypeRestrictions(restrictionS);
		
		
		/**
		#  Normalizing a numeric class will NOT be done.
		#  (Set this to not normalize the class if it's numeric).
		$ C boolean
		**/
		ValueType typeC = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionC = new TypeRestrictions();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"C" );
		optionC.setDescription("Normalizing a numeric class will NOT be done");
		optionC.setTypeRestrictions(restrictionC);

		
		
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
