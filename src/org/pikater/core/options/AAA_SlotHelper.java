package org.pikater.core.options;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;


public abstract class AAA_SlotHelper {
	
	public static List<Slot> getCAInputSlots() {
		
		Slot inputTrainingSlot = new Slot();
		inputTrainingSlot.setDescription("Training data");
		inputTrainingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTrainingSlot.setDataType("trainingData");

		
		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setDescription("Testing data");
		inputTestingSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputTestingSlot.setDataType("testingData");
		
		
		Slot inputValidationSlot = new Slot();
		inputTestingSlot.setDescription("Validation data");
		inputValidationSlot.setSlotType(SlotTypes.DATA_TYPE);
		inputValidationSlot.setDataType("validationData");
		
		Slot inputEvaluationMethodSlot = new Slot();
		inputEvaluationMethodSlot.setDescription("Evaluation Method");
		inputEvaluationMethodSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		inputEvaluationMethodSlot.setDataType("evaluationMethod");
		
		List<Slot> inputSlots = new ArrayList<Slot>();
		inputSlots.add(inputTrainingSlot);
		inputSlots.add(inputTestingSlot);
		inputSlots.add(inputValidationSlot);
		
		return inputSlots;
	}

	public static List<Slot> getCAOutputSlots() {
		
		Slot outputSlot = new Slot();
		outputSlot.setDescription("Data computed");
		outputSlot.setSlotType(SlotTypes.AGENT_TYPE);
		outputSlot.setDataType("data");
		
		List<Slot> outputSlots = new ArrayList<Slot>();
		outputSlots.add(outputSlot);
		
		return outputSlots;
	}

	public static List<Slot> getEvaluationMethodOutputSlots() {
		
		Slot outputSlot = new Slot();
		outputSlot.setDescription("Evaluation Method");
		outputSlot.setSlotType(SlotTypes.EVALUATIONMETHOD_TYPE);
		outputSlot.setDataType("evaluationMethod");
		
		List<Slot> outputSlots = new ArrayList<Slot>();
		outputSlots.add(outputSlot);
		
		return outputSlots;
	}

	public static List<Slot> getSearcherOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Parameters produced by searcher");
		outputSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		outputSlot.setDataType("parameters");
		
		List<Slot> slots = new ArrayList<Slot>();
		slots.add(outputSlot);
		
		return slots;
	}


	public static List<Slot> getRecommendOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Name of agent");
		outputSlot.setDataType(SlotTypes.RECOMMEND_TYPE);
		outputSlot.setDataType("recomend");

		List<Slot> slots = new ArrayList<Slot>();
		slots.add(outputSlot);
		
		return slots;
	}
	
}
