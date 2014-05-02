package org.pikater.core.options.xmlGenerators;

import jade.util.leap.ArrayList;

import org.pikater.core.ontology.agentInfo.Slot;
import org.pikater.core.ontology.agentInfo.slotTypes.SlotTypes;


public abstract class AAA_SlotHelper {
	
	public static ArrayList getCAInputSlots() {
		
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
		
		
		ArrayList inputSlots = new ArrayList();
		inputSlots.add(inputTrainingSlot);
		inputSlots.add(inputTestingSlot);
		inputSlots.add(inputValidationSlot);
		
		return inputSlots;
	}

	public static ArrayList getCAOutputSlots() {
		
		Slot outputSlot = new Slot();
		outputSlot.setDescription("data computed");
		outputSlot.setSlotType(SlotTypes.AGENT_TYPE);
		outputSlot.setDataType("data");
		
		ArrayList outputSlots = new ArrayList();
		outputSlots.add(outputSlot);
		
		return outputSlots;
	}


	public static ArrayList getSearcherOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Parameters produced by searcher");
		outputSlot.setSlotType(SlotTypes.SEARCH_TYPE);
		outputSlot.setDataType("parameters");
		
		ArrayList slots = new ArrayList();
		slots.add(outputSlot);
		
		return slots;
	}


	public static ArrayList getRecommendOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Name of agent");
		outputSlot.setDataType(SlotTypes.RECOMEND_TYPE);
		outputSlot.setDataType("recomend");

		ArrayList slots = new ArrayList();
		slots.add(outputSlot);
		
		return slots;
	}
	
}
