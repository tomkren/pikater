package org.pikater.core.options.xmlGenerators;

import java.util.ArrayList;

import org.pikater.core.dataStructures.slots.Slot;

public abstract class AAA_SlotHelper {
	
	public static ArrayList<Slot> getCAInputSlots() {
		
		Slot inputTrainingSlot = new Slot();
		inputTrainingSlot.setDescription("Training data");
		inputTrainingSlot.setType(Slot.SlotType.DATA_SLOT);
		inputTrainingSlot.setSex(Slot.SlotSex.CONSUMENT);
		inputTrainingSlot.setOntologyField("trainingData");

		
		Slot inputTestingSlot = new Slot();
		inputTestingSlot.setDescription("Testing data");
		inputTestingSlot.setType(Slot.SlotType.DATA_SLOT);
		inputTestingSlot.setSex(Slot.SlotSex.CONSUMENT);
		inputTestingSlot.setOntologyField("testingData");
		
		
		Slot inputValidationSlot = new Slot();
		inputTestingSlot.setDescription("Validation data");
		inputValidationSlot.setType(Slot.SlotType.DATA_SLOT);
		inputValidationSlot.setSex(Slot.SlotSex.CONSUMENT);
		inputValidationSlot.setOntologyField("validationData");
		
		
		ArrayList<Slot> inputSlots = new ArrayList<Slot>();
		inputSlots.add(inputTrainingSlot);
		inputSlots.add(inputTestingSlot);
		inputSlots.add(inputValidationSlot);
		
		return inputSlots;
	}

	public static ArrayList<Slot> getCAOutputSlots() {
		
		Slot outputSlot = new Slot();
		outputSlot.setDescription("data computed");
		outputSlot.setType(Slot.SlotType.AGENT_SLOT);
		outputSlot.setSex(Slot.SlotSex.PRODUCENT);
		outputSlot.setOntologyField(null);
		
		ArrayList<Slot> outputSlots = new ArrayList<Slot>();
		outputSlots.add(outputSlot);
		
		return outputSlots;
	}


	public static ArrayList<Slot> getSearcherOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Parameters produced by searcher");
		outputSlot.setType(Slot.SlotType.SEARCHERCH_SLOT);
		outputSlot.setSex(Slot.SlotSex.PRODUCENT);
		outputSlot.setOntologyField(null);
		
		ArrayList<Slot> slots = new ArrayList<Slot>();
		slots.add(outputSlot);
		
		return slots;
	}


	public static ArrayList<Slot> getRecommendOutputSlots() {

		Slot outputSlot = new Slot();
		outputSlot.setDescription("Name of agent");
		outputSlot.setType(Slot.SlotType.RECOMMEND_SLOT);
		outputSlot.setSex(Slot.SlotSex.PRODUCENT);
		outputSlot.setOntologyField(null);
		
		ArrayList<Slot> slots = new ArrayList<Slot>();
		slots.add(outputSlot);
		
		return slots;
	}
	
}
