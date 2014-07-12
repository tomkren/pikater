package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_GASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;

public class GASearch_SearchBox {
	
	public static AgentInfo get() {

		Type typeE = new Type(FloatValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestriction restrictionE = new TypeRestriction();
		restrictionE.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new FloatValue(0.1f),
				new Type(FloatValue.class),
				"E" );
		optionE.setDescription("Minimum error rate");
		optionE.setPossibleTypesRestriction(restrictionE);


		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		TypeRestriction restrictionM = new TypeRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("Maximal number of generations");
		optionM.setPossibleTypesRestriction(restrictionM);

		
		Type typeT = new Type(FloatValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestriction restrictionT = new TypeRestriction();
		restrictionT.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new FloatValue(0.2f),
				new Type(FloatValue.class),
				"T" );
		optionT.setDescription("Mutation rate");
		optionT.setPossibleTypesRestriction(restrictionT);

		
		Type typeX = new Type(FloatValue.class);
		typeX.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		TypeRestriction restrictionX = new TypeRestriction();
		restrictionX.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeX )) ));
		
		NewOption optionX = new NewOption(
				new FloatValue(0.5f),
				new Type(FloatValue.class),
				"X" );
		optionX.setDescription("Crossover probability");
		optionX.setPossibleTypesRestriction(restrictionX);

		
		Type typeP = new Type(IntegerValue.class);
		typeP.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestriction restrictionP = new TypeRestriction();
		restrictionP.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new IntegerValue(5),
				new Type(IntegerValue.class),
				"P" );
		optionP.setDescription("Population size");
		optionP.setPossibleTypesRestriction(restrictionP);


		Type typeS = new Type(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		TypeRestriction restrictionS = new TypeRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(2),
				new Type(IntegerValue.class),
				"S" );
		optionS.setDescription("Size of tournament in selection");
		optionS.setPossibleTypesRestriction(restrictionS);


		
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
