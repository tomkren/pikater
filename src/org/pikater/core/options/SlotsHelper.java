package org.pikater.core.options;

import java.util.Arrays;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;

public abstract class SlotsHelper
{
	//-------------------------------------------------------
	// LISTS OF SLOTS
	
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

		return Arrays.asList(
				inputTrainingSlot,
				inputTestingSlot,
				inputValidationSlot,
				getSlot_EvaluationMethod()
		);
	}

	public static List<Slot> getSlots_CAOutput()
	{
		return Arrays.asList(getSlot_ComputedData());
	}

	public static List<Slot> getSlots_EvaluationMethodOutput()
	{
		return Arrays.asList(getSlot_EvaluationMethod());
	}

	public static List<Slot> getSlots_SearchOutput()
	{
		return Arrays.asList(getSlot_ParametersProducedBySearch());
	}

	public static List<Slot> getSlots_RecommendOutput()
	{
		return Arrays.asList(getSlot_RecommendedAgent());
	}
	
	//-------------------------------------------------------
	// INDIVIDUAL SLOT DEFINITIONS
	
	public static Slot getSlot_EvaluationMethod()
	{
		Slot result = new Slot();
		result.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		result.setDataType("evaluationMethod");
		return result;
	}
	
	public static Slot getSlot_RecommendedAgent()
	{
		Slot result = new Slot();
		result.setDataType(SlotTypes.RECOMMEND_TYPE);
		result.setDataType("recommend");
		result.setDescription("Recommends an agent to use (its name).");
		return result;
	}
	
	public static Slot getSlot_ParametersProducedBySearch()
	{
		Slot result = new Slot();
		result.setSlotType(SlotTypes.SEARCH_TYPE);
		result.setDataType("parameters");
		result.setDescription("Parameters produced by search.");
		return result;
	}
	
	public static Slot getSlot_ComputedData()
	{
		Slot result = new Slot();
		result.setSlotType(SlotTypes.AGENT_TYPE);
		result.setDataType("computedData");
		result.setDescription("Data computed by an agent.");
		return result;
	}

}