package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_EASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;


public class EASearch_SearchBox {

	public static AgentInfo get() {

		Type typeE = new Type(FloatValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionE = new PossibleTypesRestriction();
		restrictionE.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new FloatValue(0.1f),
				new Type(FloatValue.class),
				"E" );
		optionE.setDescription("Minimum error rate");
		optionE.setPossibleTypesRestriction(restrictionE);

//TODO: chyba
		Type typeM = new Type(FloatValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		PossibleTypesRestriction restrictionM = new PossibleTypesRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new FloatValue(0.1f),
				new Type(FloatValue.class),
				"M" );
		optionM.setDescription("Maximal number of generations");
		optionM.setPossibleTypesRestriction(restrictionM);


		Type typeT = new Type(FloatValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionT = new PossibleTypesRestriction();
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
		PossibleTypesRestriction restrictionX = new PossibleTypesRestriction();
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
		PossibleTypesRestriction restrictionP = new PossibleTypesRestriction();
		restrictionP.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"P" );
		optionP.setDescription("Population size");
		optionP.setPossibleTypesRestriction(restrictionP);
		

		Type typeI = new Type(IntegerValue.class);
		typeI.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		PossibleTypesRestriction restrictionI = new PossibleTypesRestriction();
		restrictionI.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new IntegerValue(10),
				new Type(IntegerValue.class),
				"I" );
		optionI.setDescription("Maximum number of option evaluations");
		optionI.setPossibleTypesRestriction(restrictionI);


		Type typeF = new Type(FloatValue.class);
		typeF.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionF = new PossibleTypesRestriction();
		restrictionF.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeF )) ));
		
		NewOption optionF = new NewOption(
				new FloatValue(0.2f),
				new Type(FloatValue.class),
				"F" );
		optionF.setDescription("Mutation rate per field in individual");
		optionF.setPossibleTypesRestriction(restrictionF);



		Type typeL = new Type(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		PossibleTypesRestriction restrictionL = new PossibleTypesRestriction();
		restrictionL.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(0.1f),
				new Type(FloatValue.class),
				"L" );
		optionL.setDescription("The percentage of elite individuals");
		optionL.setPossibleTypesRestriction(restrictionL);

		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_EASearch.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("EASearch");
		agentInfo.setDescription("Searcher using Evolution algorithm");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionX);
		agentInfo.addOption(optionP);
		agentInfo.addOption(optionI);
		agentInfo.addOption(optionF);
		agentInfo.addOption(optionL);

		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());
		
		return agentInfo;
	}
}
