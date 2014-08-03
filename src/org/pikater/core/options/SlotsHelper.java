package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;

public abstract class SlotsHelper
{
	//-------------------------------------------------------
	// INDIVIDUAL SLOT DEFINITIONS
	
	public static List<Slot> getSlots_CARecSearchComplexIntput()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setDataType(SlotTypes.RECOMMEND_TYPE);
		recomedSlot.setDataType("recommend");
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.AGENT_TYPE);
		comAgentSlot.setDataType("computedData");
		comAgentSlot.setDescription("Data computed by an agent.");
		
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		searchSlot.setDataType("search");
		searchSlot.setDescription("Parameters produced by search.");
		
		return Arrays.asList(
				recomedSlot,
				comAgentSlot,
				searchSlot);
	}
	
	public static List<Slot> getSlots_CARecSearchComplexOutput()
	{
		return getSlots_CAOutput();
	}
	
	public static List<Slot> getSlots_CAInput()
	{
		Slot inputTrainingSlot = new Slot();
		inputTrainingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTrainingSlot.setDataType("trainingData");

		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTestingSlot.setDataType("testingData");

		Slot inputValidationSlot = new Slot();
		inputValidationSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputValidationSlot.setDataType("validationData");

		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		evaluationMethodSlot.setDataType("evaluationMethod");
		
		return Arrays.asList(
				inputTrainingSlot,
				inputTestingSlot,
				inputValidationSlot,
				evaluationMethodSlot
		);
	}

	public static List<Slot> getSlots_CAOutput()
	{
		Slot comAgentSlot = new Slot();
		comAgentSlot.setSlotType(SlotTypes.AGENT_TYPE);
		comAgentSlot.setDataType("computedData");
		comAgentSlot.setDescription("Data computed by an agent.");
		
		return Arrays.asList(comAgentSlot);
	}

	public static List<Slot> getSlots_EvaluationMethodOutput()
	{
		return Arrays.asList(getSlot_EvaluationMethodOutput());
	}

	public static List<Slot> getSlots_SearchOutput()
	{
		Slot searchSlot = new Slot();
		searchSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		searchSlot.setDataType("search");
		searchSlot.setDescription("Parameters produced by search.");
		
		return Arrays.asList(searchSlot);
	}

	public static List<Slot> getSlots_RecommendOutput()
	{
		Slot recomedSlot = new Slot();
		recomedSlot.setDataType(SlotTypes.RECOMMEND_TYPE);
		recomedSlot.setDataType("recommend");
		recomedSlot.setDescription("Recommends an agent to use (its name).");
		
		return Arrays.asList(recomedSlot);
	}
	
	public static Slot getSlot_EvaluationMethodOutput()
	{
		Slot evaluationMethodSlot = new Slot();
		evaluationMethodSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		evaluationMethodSlot.setDataType("evaluationMethod");
		return evaluationMethodSlot;
	}
	
	
	


}