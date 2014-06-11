package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.value.NullValue;

public class J48_CABox {
	
	public static AgentInfo get() {
		
		Type typeU = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionU = new PossibleTypesRestriction();
		restrictionU.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeU )) ));
		
		NewOption optionU = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"U" );
		optionU.setDescription("Use unpruned tree");
		optionU.setPossibleTypesRestriction(restrictionU);
		
		
		List<IValue> valuesC = new ArrayList<IValue>();
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new FloatValue(0.0001f));
		valuesC.add(new FloatValue(0.1f));
		valuesC.add(new FloatValue(0.2f));
		valuesC.add(new FloatValue(0.25f));
		valuesC.add(new FloatValue(0.3f));
		valuesC.add(new FloatValue(0.4f));
				
		Type typeC = new Type(FloatValue.class);
		typeC.setSetRestriction(new SetRestriction(valuesC));
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(0.25f),
				new Type(FloatValue.class),
				"C" );
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		optionC.setPossibleTypesRestriction(restrictionC);
		
		
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
		
				
		Type typeR = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionR = new PossibleTypesRestriction();
		restrictionR.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"R" );
		optionR.setDescription("Use reduced error pruning. No subtree raising is performed");
		optionR.setPossibleTypesRestriction(restrictionR);

		
		Type typeN = new Type(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10) ));
		PossibleTypesRestriction restrictionN = new PossibleTypesRestriction();
		restrictionN.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(3),
				new Type(IntegerValue.class),
				"N" );
		optionN.setDescription("Set minimum number of instances per leaf");
		optionN.setPossibleTypesRestriction(restrictionN);

		
		Type typeB = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionB = new PossibleTypesRestriction();
		restrictionB.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"B" );
		optionB.setDescription("Use binary splits for nominal attributes");
		optionB.setPossibleTypesRestriction(restrictionB);
		

		Type typeS = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionS = new PossibleTypesRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"S" );
		optionS.setDescription("Don't perform subtree raising");
		optionS.setPossibleTypesRestriction(restrictionB);
		
		
		Type typeA = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionA = new PossibleTypesRestriction();
		restrictionA.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeA )) ));
		
		NewOption optionA = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"A" );
		optionA.setDescription("If set, Laplace smoothing is used for predicted probabilites");
		optionA.setPossibleTypesRestriction(restrictionA);


		Type typeQ = new Type(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		PossibleTypesRestriction restrictionQ = new PossibleTypesRestriction();
		restrictionQ.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeQ )) ));
		
		NewOption optionQ = new NewOption(
				new IntegerValue(3),
				new Type(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setPossibleTypesRestriction(restrictionQ);
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaJ48.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("J48");
		agentInfo.setDescription("J48 method description");

		agentInfo.addOption(optionU);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionQ);


		//Slot Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());

		return agentInfo;
	}

}
