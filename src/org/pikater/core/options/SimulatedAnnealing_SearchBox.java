package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.search.Agent_SimulatedAnnealing;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.TypeRestrictions;
import org.pikater.core.ontology.subtrees.newOption.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.valuetypes.IntegerValue;

public class SimulatedAnnealing_SearchBox {

	public static AgentInfo get() {

		ValueType typeE = new ValueType(DoubleValue.class);
		typeE.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		TypeRestrictions restrictionE = new TypeRestrictions();
		restrictionE.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeE )) ));
		
		NewOption optionE = new NewOption(
				new DoubleValue(0.1),
				new ValueType(DoubleValue.class),
				"E" );
		optionE.setDescription("Set minimum number of instances per leaf");
		optionE.setTypeRestrictions(restrictionE);
		
		
		ValueType typeM = new ValueType(IntegerValue.class);
		typeM.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(1), new IntegerValue(1000)) );
		TypeRestrictions restrictionM = new TypeRestrictions();
		restrictionM.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeM )) ));
		
		NewOption optionM = new NewOption(
				new IntegerValue(50),
				new ValueType(IntegerValue.class),
				"M" );
		optionM.setDescription("M");
		optionM.setTypeRestrictions(restrictionM);
		

		ValueType typeT = new ValueType(DoubleValue.class);
		typeT.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(100.0) ));
		TypeRestrictions restrictionT = new TypeRestrictions();
		restrictionT.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeT )) ));
		
		NewOption optionT = new NewOption(
				new DoubleValue(1.0),
				new ValueType(DoubleValue.class),
				"T" );
		optionT.setDescription("T");
		optionT.setTypeRestrictions(restrictionT);
		
		
		ValueType typeS = new ValueType(DoubleValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new DoubleValue(0.0), new DoubleValue(1.0) ));
		TypeRestrictions restrictionS = new TypeRestrictions();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new DoubleValue(0.5),
				new ValueType(DoubleValue.class),
				"S" );
		optionS.setDescription("T");
		optionS.setTypeRestrictions(restrictionS);
		
		
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