package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaLinearRegression;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restriction.PossibleTypesRestriction;
import org.pikater.core.ontology.subtrees.newOption.restriction.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.type.Type;
import org.pikater.core.ontology.subtrees.newOption.type.Types;
import org.pikater.core.ontology.subtrees.newOption.value.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.value.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.value.IntegerValue;

public class LinearRegression_CABox {

	public static AgentInfo get() {
				
		/*
		 
		# name, type, number od values, parameters range / set
		# r ... range
		# s ... set  (example: s 1, 2, 3, 4, 5, 6, 7, 8)
		# 
		
		# Set the attriute selection method to use. 1 = None, 2 = Greedy (default 0 = M5' method)
		$ S int 1 1 r 0 2
		
		# Do not try to eliminate colinear attributes
		$ C boolean
		
		# The ridge parameter (default 1.0e-8)
		$ R float 1 1 r 0.0000000001 0.0001 

		*/
		Type typeS = new Type(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(2) ));
		PossibleTypesRestriction restrictionS = new PossibleTypesRestriction();
		restrictionS.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(6),
				new Type(IntegerValue.class),
				"S" );
		optionS.setDescription("Set the attriute selection method to use. 1 = None, 2 = Greedy (default 0 = M5' method)");
		optionS.setPossibleTypesRestriction(restrictionS);
		
		
		Type typeC = new Type(BooleanValue.class);
		PossibleTypesRestriction restrictionC = new PossibleTypesRestriction();
		restrictionC.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new Type(BooleanValue.class),
				"C" );
		optionC.setDescription("Do not try to eliminate colinear attributes");
		optionC.setPossibleTypesRestriction(restrictionC);
		

		Type typeR = new Type(FloatValue.class);
		typeR.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0000000001f), new FloatValue(0.0001f) ));
		PossibleTypesRestriction restrictionR = new PossibleTypesRestriction();
		restrictionR.addPossibleValues( new Types(
				new ArrayList<Type>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new FloatValue(0.00000001f),
				new Type(FloatValue.class),
				"R" );
		optionR.setDescription("The ridge parameter (default 1.0e-8)");
		optionR.setPossibleTypesRestriction(restrictionR);
		
	
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentClass(Agent_WekaLinearRegression.class);
		agentInfo.setOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("LinearRegression");
		agentInfo.setDescription("Linear Regression");
	
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionR);
		
	
		// Slots Definition
		agentInfo.setInputSlots(AAA_SlotHelper.getCAInputSlots());
		agentInfo.setOutputSlots(AAA_SlotHelper.getCAOutputSlots());
	
		return agentInfo;
	}
	
}
