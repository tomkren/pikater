package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_GASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;

public class GASearch_SearchBox {
	
	public static AgentInfo get() {

		ValueType typeE = new ValueType(FloatValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestrictions restrictionE = new TypeRestrictions();
		restrictionE.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new FloatValue(0.1f),
				new ValueType(FloatValue.class),
				"E" );
		optionE.setDescription("Minimum error rate");
		optionE.setTypeRestrictions(restrictionE);


		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		TypeRestrictions restrictionM = new TypeRestrictions();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("Maximal number of generations");
		optionM.setTypeRestrictions(restrictionM);

		
		ValueType typeT = new ValueType(FloatValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestrictions restrictionT = new TypeRestrictions();
		restrictionT.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new FloatValue(0.2f),
				new ValueType(FloatValue.class),
				"T" );
		optionT.setDescription("Mutation rate");
		optionT.setTypeRestrictions(restrictionT);

		
		ValueType typeX = new ValueType(FloatValue.class);
		typeX.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestrictions restrictionX = new TypeRestrictions();
		restrictionX.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeX )) ));
		
		NewOption optionX = new NewOption(
				new FloatValue(0.5f),
				new ValueType(FloatValue.class),
				"X" );
		optionX.setDescription("Crossover probability");
		optionX.setTypeRestrictions(restrictionX);

		
		ValueType typeP = new ValueType(IntegerValue.class);
		typeP.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestrictions restrictionP = new TypeRestrictions();
		restrictionP.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new IntegerValue(5),
				new ValueType(IntegerValue.class),
				"P" );
		optionP.setDescription("Population size");
		optionP.setTypeRestrictions(restrictionP);


		ValueType typeS = new ValueType(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestrictions restrictionS = new TypeRestrictions();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(2),
				new ValueType(IntegerValue.class),
				"S" );
		optionS.setDescription("Size of tournament in selection");
		optionS.setTypeRestrictions(restrictionS);


		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_GASearch.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("GASearch");
		agentInfo.setDescription("Searcher using Genetic algorithm");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionX);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionS);


		//Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());

		return agentInfo;
	}

}
