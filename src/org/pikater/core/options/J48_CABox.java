package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.ITypedValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.NullValue;

public class J48_CABox {
	
	public static AgentInfo get() {
		
		ValueType typeU = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionU = new TypeRestrictions();
		restrictionU.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeU )) ));
		
		NewOption optionU = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"U" );
		optionU.setDescription("Use unpruned tree");
		optionU.setTypeRestrictions(restrictionU);
		
		
		List<ITypedValue> valuesC = new ArrayList<ITypedValue>();
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new NullValue());
		valuesC.add(new FloatValue(0.0001f));
		valuesC.add(new FloatValue(0.1f));
		valuesC.add(new FloatValue(0.2f));
		valuesC.add(new FloatValue(0.25f));
		valuesC.add(new FloatValue(0.3f));
		valuesC.add(new FloatValue(0.4f));
				
		ValueType typeC = new ValueType(FloatValue.class);
		typeC.setSetRestriction(new SetRestriction(valuesC));
		TypeRestrictions restrictionC = new TypeRestrictions();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new FloatValue(0.25f),
				new ValueType(FloatValue.class),
				"C" );
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		optionC.setTypeRestrictions(restrictionC);
		
		
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10) ));
		TypeRestrictions restrictionM = new TypeRestrictions();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(2),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("Set minimum number of instances per leaf");
		optionM.setTypeRestrictions(restrictionM);
		
				
		ValueType typeR = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionR = new TypeRestrictions();
		restrictionR.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"R" );
		optionR.setDescription("Use reduced error pruning. No subtree raising is performed");
		optionR.setTypeRestrictions(restrictionR);

		
		ValueType typeN = new ValueType(IntegerValue.class);
		typeN.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(10) ));
		TypeRestrictions restrictionN = new TypeRestrictions();
		restrictionN.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeN )) ));
		
		NewOption optionN = new NewOption(
				new IntegerValue(3),
				new ValueType(IntegerValue.class),
				"N" );
		optionN.setDescription("Set minimum number of instances per leaf");
		optionN.setTypeRestrictions(restrictionN);

		
		ValueType typeB = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionB = new TypeRestrictions();
		restrictionB.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeB )) ));
		
		NewOption optionB = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"B" );
		optionB.setDescription("Use binary splits for nominal attributes");
		optionB.setTypeRestrictions(restrictionB);
		

		ValueType typeS = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionS = new TypeRestrictions();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"S" );
		optionS.setDescription("Don't perform subtree raising");
		optionS.setTypeRestrictions(restrictionB);
		
		
		ValueType typeA = new ValueType(BooleanValue.class);
		TypeRestrictions restrictionA = new TypeRestrictions();
		restrictionA.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeA )) ));
		
		NewOption optionA = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"A" );
		optionA.setDescription("If set, Laplace smoothing is used for predicted probabilites");
		optionA.setTypeRestrictions(restrictionA);


		ValueType typeQ = new ValueType(IntegerValue.class);
		typeQ.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(Integer.MAX_VALUE) ));
		TypeRestrictions restrictionQ = new TypeRestrictions();
		restrictionQ.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeQ )) ));
		
		NewOption optionQ = new NewOption(
				new IntegerValue(3),
				new ValueType(IntegerValue.class),
				"Q" );
		optionQ.setDescription("The seed for reduced-error pruning");
		optionQ.setTypeRestrictions(restrictionQ);
		

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
