package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaPARTCA;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.NullValue;

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
		ValueType typeC = new ValueType(FloatValue.class);
		typeC.setSetRestriction(
				new SetRestriction( list ) );
		RestrictionsForOption restrictionC = new RestrictionsForOption();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(0.25f),
				new ValueType(FloatValue.class),
				"C" );
		optionC.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionC.setTypeRestrictions(restrictionC);
		
		
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10) ));
		RestrictionsForOption restrictionM = new RestrictionsForOption();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(2),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("Set minimum number of instances per leaf");
		optionM.setTypeRestrictions(restrictionM);
		
		
		/**
		# Use reduced error pruning.
		$ R boolean
		**/
		ValueType typeR = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionR = new RestrictionsForOption();
		restrictionR.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"R" );
		optionR.setDescription("Use reduced error pruning");
		optionR.setTypeRestrictions(restrictionR);


		
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
		ValueType typeN = new ValueType(FloatValue.class);
		typeN.setSetRestriction(
				new SetRestriction( listN ) );
		RestrictionsForOption restrictionN = new RestrictionsForOption();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(3),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Set the number of folder to use in the computing of the mutual information");
		optionN.setTypeRestrictions(restrictionN);


		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		ValueType typeB = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionB = new RestrictionsForOption();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"B" );
		optionB.setDescription("Use reduced error pruning");
		optionB.setTypeRestrictions(restrictionB);


		/**
		# Generate unpruned decision list.
		$ U boolean
		**/
		ValueType typeU = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionU = new RestrictionsForOption();
		restrictionU.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeU )) ));
		
		NewOption optionU = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"U" );
		optionU.setDescription("Generate unpruned decision list");
		optionU.setTypeRestrictions(restrictionU);
		
		
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		ValueType typeQ = new ValueType(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		RestrictionsForOption restrictionQ = new RestrictionsForOption();
		restrictionQ.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeQ )) ));
		
		NewOption optionQ = new NewOption(
				new IntegerValue(1),
				new ValueType(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setTypeRestrictions(restrictionQ);
		
		

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
