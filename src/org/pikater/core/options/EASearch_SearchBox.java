package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_EASearch;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;


public class EASearch_SearchBox {

	public static AgentInfo get() {

		ValueType typeE = new ValueType(FloatValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		RestrictionsForOption restrictionE = new RestrictionsForOption();
		restrictionE.add(new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new FloatValue(0.1f),
				new ValueType(FloatValue.class),
				"E" );
		optionE.setDescription("Minimum error rate");
		optionE.setTypeRestrictions(restrictionE);

//TODO: chyba
		ValueType typeM = new ValueType(FloatValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000) ));
		RestrictionsForOption restrictionM = new RestrictionsForOption();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new FloatValue(0.1f),
				new ValueType(FloatValue.class),
				"M" );
		optionM.setDescription("Maximal number of generations");
		optionM.setTypeRestrictions(restrictionM);


		ValueType typeT = new ValueType(FloatValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		RestrictionsForOption restrictionT = new RestrictionsForOption();
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
		RestrictionsForOption restrictionX = new RestrictionsForOption();
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
		RestrictionsForOption restrictionP = new RestrictionsForOption();
		restrictionP.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeP )) ));
		
		NewOption optionP = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"P" );
		optionP.setDescription("Population size");
		optionP.setTypeRestrictions(restrictionP);
		

		ValueType typeI = new ValueType(IntegerValue.class);
		typeI.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(100) ));
		RestrictionsForOption restrictionI = new RestrictionsForOption();
		restrictionI.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeI )) ));
		
		NewOption optionI = new NewOption(
				new IntegerValue(10),
				new ValueType(IntegerValue.class),
				"I" );
		optionI.setDescription("Maximum number of option evaluations");
		optionI.setTypeRestrictions(restrictionI);


		ValueType typeF = new ValueType(FloatValue.class);
		typeF.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		RestrictionsForOption restrictionF = new RestrictionsForOption();
		restrictionF.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeF )) ));
		
		NewOption optionF = new NewOption(
				new FloatValue(0.2f),
				new ValueType(FloatValue.class),
				"F" );
		optionF.setDescription("Mutation rate per field in individual");
		optionF.setTypeRestrictions(restrictionF);



		ValueType typeL = new ValueType(FloatValue.class);
		typeL.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0f), new FloatValue(1.0f) ));
		RestrictionsForOption restrictionL = new RestrictionsForOption();
		restrictionL.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeL )) ));
		
		NewOption optionL = new NewOption(
				new FloatValue(0.1f),
				new ValueType(FloatValue.class),
				"L" );
		optionL.setDescription("The percentage of elite individuals");
		optionL.setTypeRestrictions(restrictionL);

		

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
