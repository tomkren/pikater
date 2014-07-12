package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.typedValue.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.typedValue.IntegerValue;

public class SimulatedAnnealing_SearchBox {

	public static AgentInfo get() {

		Type typeE = new Type(DoubleValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		TypeRestriction restrictionE = new TypeRestriction();
		restrictionE.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new DoubleValue(0.1),
				new Type(DoubleValue.class),
				"E" );
		optionE.setDescription("Set minimum number of instances per leaf");
		optionE.setPossibleTypesRestriction(restrictionE);
		
		
		Type typeM = new Type(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000)) );
		TypeRestriction restrictionM = new TypeRestriction();
		restrictionM.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(50),
				new Type(IntegerValue.class),
				"M" );
		optionM.setDescription("M");
		optionM.setPossibleTypesRestriction(restrictionM);
		

		Type typeT = new Type(DoubleValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(100.0) ));
		TypeRestriction restrictionT = new TypeRestriction();
		restrictionT.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new DoubleValue(1.0),
				new Type(DoubleValue.class),
				"T" );
		optionT.setDescription("T");
		optionT.setPossibleTypesRestriction(restrictionT);
		
		
		Type typeS = new Type(DoubleValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		TypeRestriction restrictionS = new TypeRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new DoubleValue(0.5),
				new Type(DoubleValue.class),
				"S" );
		optionS.setDescription("T");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_SimulatedAnnealing.class);
		agentInfo.setOntologyClass(Search.class);
	
		agentInfo.setName("SimulatedAnnealing-Searcher");
		agentInfo.setDescription("Searches defined parameters and provides them in output slots. Simulated annealing is used for searching.");

		agentInfo.addOption(optionE);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionT);
		agentInfo.addOption(optionS);

		// Slot Definition
		agentInfo.setOutputSlots(AAA_SlotHelper.getSearcherOutputSlots());		

		return agentInfo;
	}

}