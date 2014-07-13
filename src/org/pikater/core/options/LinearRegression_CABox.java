package org.pikater.core.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaLinearRegression;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newOption.RestrictionsForOption;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;

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
		ValueType typeS = new ValueType(IntegerValue.class);
		typeS.setRangeRestriction(
				new RangeRestriction(
						new IntegerValue(0), new IntegerValue(2) ));
		RestrictionsForOption restrictionS = new RestrictionsForOption();
		restrictionS.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeS )) ));
		
		NewOption optionS = new NewOption(
				new IntegerValue(6),
				new ValueType(IntegerValue.class),
				"S" );
		optionS.setDescription("Set the attriute selection method to use. 1 = None, 2 = Greedy (default 0 = M5' method)");
		optionS.setTypeRestrictions(restrictionS);
		
		
		ValueType typeC = new ValueType(BooleanValue.class);
		RestrictionsForOption restrictionC = new RestrictionsForOption();
		restrictionC.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeC )) ));
		
		NewOption optionC = new NewOption(
				new BooleanValue(false),
				new ValueType(BooleanValue.class),
				"C" );
		optionC.setDescription("Do not try to eliminate colinear attributes");
		optionC.setTypeRestrictions(restrictionC);
		

		ValueType typeR = new ValueType(FloatValue.class);
		typeR.setRangeRestriction(
				new RangeRestriction(
						new FloatValue(0.0000000001f), new FloatValue(0.0001f) ));
		RestrictionsForOption restrictionR = new RestrictionsForOption();
		restrictionR.add( new TypeRestriction(
				new ArrayList<ValueType>(Arrays.asList( typeR )) ));
		
		NewOption optionR = new NewOption(
				new FloatValue(0.00000001f),
				new ValueType(FloatValue.class),
				"R" );
		optionR.setDescription("The ridge parameter (default 1.0e-8)");
		optionR.setTypeRestrictions(restrictionR);
		
	
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
