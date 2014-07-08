package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaPARTCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.NullValue;

public class PART_CABox {
	
	public static AgentInfo get() {

		/**
		# Set confidence threshold for pruning. (Default: 0.25)
		# $ C float 1 1 r 0.0001 0.4 
		$ C float 1 1 s null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4, 0.5
		**/
		List<ITypedValue> list = new ArrayList<ITypedValue>(
				Arrays.asList(
						new NullValue(), new FloatValue(0.0001f),
						new FloatValue(0.1f), new FloatValue(0.2f),
						new FloatValue(0.25f), new FloatValue(0.3f),
						new FloatValue(0.4f), new FloatValue(0.5f)
				));
		Type typeC = new Type(FloatValue.class);
		typeC.setSetRestriction(
				new SetRestriction( list ) );
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(0.25f),
				new Type(FloatValue.class),
				"C" );
		optionC.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionC.setPossibleTypesRestriction(restrictionC);
		
		
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(2),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("Set minimum number of instances per leaf");
		optionM.setPossibleTypesRestriction(restrictionM);
		
		
		/**
		# Use reduced error pruning.
		$ R boolean
		**/
		Type typeR = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionR = new PossibleTypesRestriction();
		restrictionR.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"R" );
		optionR.setDescription("Use reduced error pruning");
		optionR.setPossibleTypesRestriction(restrictionR);


		
		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		List<ITypedValue> listN = new ArrayList<ITypedValue>(
				Arrays.asList(
						new NullValue(), new IntegerValue(1),
						new IntegerValue(2), new IntegerValue(3),
						new IntegerValue(4), new IntegerValue(5)
				));
		Type typeN = new Type(FloatValue.class);
		typeN.setSetRestriction(
				new SetRestriction( listN ) );
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(3),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionN.setPossibleTypesRestriction(restrictionN);


		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		Type typeB = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"B" );
		optionB.setDescription("Use reduced error pruning");
		optionB.setPossibleTypesRestriction(restrictionB);


		/**
		# Generate unpruned decision list.
		$ U boolean
		**/
		Type typeU = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionU = new PossibleTypesRestriction();
		restrictionU.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeU )) ));
		
		NewOption optionU = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"U" );
		optionU.setDescription("Generate unpruned decision list");
		optionU.setPossibleTypesRestriction(restrictionU);
		
		
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		Type typeQ = new Type(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionQ = new PossibleTypesRestriction();
		restrictionQ.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeQ )) ));
		
		NewOption optionQ = new NewOption(
				new IntegerValue(1),
				new Type(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setPossibleTypesRestriction(restrictionQ);
		
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaPARTCA.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
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
